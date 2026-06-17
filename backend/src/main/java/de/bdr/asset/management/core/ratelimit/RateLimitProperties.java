package de.bdr.asset.management.core.ratelimit;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {

    private Map<String, EndpointLimit> endpoints = new HashMap<>();

    public record EndpointLimit(
            long capacity,
            long refillTokens,
            Duration refillPeriod
    ) {
        public EndpointLimit {
            if (capacity <= 0)
                throw new IllegalArgumentException("capacity must be > 0");
            if (refillTokens <= 0)
                throw new IllegalArgumentException("refillTokens must be > 0");
            if (refillPeriod == null || refillPeriod.isNegative())
                throw new IllegalArgumentException("refillPeriod must be positive");
        }
    }
}