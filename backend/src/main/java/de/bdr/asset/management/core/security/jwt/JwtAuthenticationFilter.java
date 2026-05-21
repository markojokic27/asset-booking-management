package de.bdr.asset.management.core.security.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter guarantees the filter runs exactly once per request,
    // even in forward/include chains.

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider,
                                   UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, java.io.IOException {

        String header = request.getHeader("Authorization");

        // No Authorization header, or not a Bearer token — skip JWT processing.
        // The request continues; Spring Security will reject it if authentication is required.
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Strip "Bearer " prefix to get the raw token
        String token = header.substring(7);

        try {
            String username = tokenProvider.extractUsername(token);

            // Only set authentication if we have a username and no authentication yet.
            // This avoids overwriting existing authentication (e.g., from another filter).
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (tokenProvider.isValid(token, userDetails)) {
                    // Create authentication token with granted authorities (roles)
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    // Add request details (IP address, session ID) for audit logging
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Store in SecurityContext — available to the rest of the filter chain
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (JwtException e) {
            // Invalid token (expired, tampered, wrong signature) — clear context and continue.
            // The request will be rejected as unauthenticated if it requires authentication.
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

}
