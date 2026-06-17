package de.bdr.asset.management.core.ratelimit;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final String HEADER_RETRY_AFTER = "Retry-After";

    private final RateLimitProperties properties;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final Map<String, Instant> lastAccess = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String path = request.getServletPath();

        for (var entry : properties.getEndpoints().entrySet()) {
            String pattern = entry.getKey();

            if (!matches(pattern, method, path)) {
                continue;
            }

            String clientIp = resolveClientIp(request);
            String bucketKey = method + ":" + clientIp + ":" + pattern;

            Bucket bucket = buckets.computeIfAbsent(bucketKey, k -> {
                RateLimitProperties.EndpointLimit limit = entry.getValue();
                Bandwidth bandwidth = Bandwidth.builder()
                        .capacity(limit.capacity())
                        .refillIntervally(limit.refillTokens(), limit.refillPeriod())
                        .build();
                return Bucket.builder().addLimit(bandwidth).build();
            });

            lastAccess.put(bucketKey, Instant.now());

            if (bucket.tryConsume(1)) {
                chain.doFilter(request, response);
                return;
            }

            long remainingSeconds = entry.getValue().refillPeriod().toSeconds();

            log.warn("Rate limit exceeded for IP {} on {} {}", clientIp, method, path);

            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Rate limit exceeded. Try again in " + remainingSeconds + " seconds."
            );
            problem.setTitle("Too Many Requests");
            problem.setInstance(URI.create(path));
            problem.setProperty("timestamp", Instant.now());

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            response.setHeader(HEADER_RETRY_AFTER, String.valueOf(remainingSeconds));
            response.getWriter().write(objectMapper.writeValueAsString(problem));
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean matches(String pattern, String method, String path) {
        int colon = pattern.indexOf(':');
        if (colon == -1) {
            return pathMatcher.match(pattern, path);
        }
        String patternMethod = pattern.substring(0, colon);
        String patternPath = pattern.substring(colon + 1);
        return patternMethod.equalsIgnoreCase(method) && pathMatcher.match(patternPath, path);
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].strip();
        }
        return request.getRemoteAddr();
    }

    @Scheduled(fixedRate = 60_000)
    public void evictStaleBuckets() {
        Instant cutoff = Instant.now().minus(Duration.ofMinutes(5));
        lastAccess.entrySet().removeIf(entry -> {
            if (entry.getValue().isBefore(cutoff)) {
                buckets.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }
}