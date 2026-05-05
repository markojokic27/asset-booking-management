package de.bdr.asset.management.core.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** Base64-encoded secret key for signing JWTs */
    private String secret;

    /** Access token expiration in seconds (e.g., 900 = 15 minutes) */
    private long accessTokenExpiry;

    /** Refresh token expiration in seconds (e.g., 604800 = 7 days) */
    private long refreshTokenExpiry;

}