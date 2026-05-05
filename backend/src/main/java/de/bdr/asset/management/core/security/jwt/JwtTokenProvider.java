package de.bdr.asset.management.core.security.jwt;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import de.bdr.asset.management.core.security.userdetails.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // Derives the signing key from the base64-encoded secret in application.yml
    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    // Helper method where we define what fields are included in the JWT token.
    private Map<String, Object> buildClaims(CustomUserDetails userDetails) {
        return Map.of(
            "userId", userDetails.getId(),
            "name", userDetails.getName(),
            "surname", userDetails.getSurname(),
            "email", userDetails.getEmail(),
            "benefit", userDetails.getBenefit(),
            "roles", (userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList())  // ["ROLE_EMPLOYEE"], ["ROLE_MANAGER"] or ["ROLE_ADMIN"]
        );
    }

    // Access token: short-lived (15 min); contains roles for authorization decisions
    public String generateAccessToken(UserDetails userDetails)
    {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(buildClaims((CustomUserDetails) userDetails))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()
                        + jwtProperties.getAccessTokenExpiry() * 1000))
                .signWith(secretKey())                     // HS256 HMAC signature
                .compact();
    }

    // Refresh token: long-lived (7 days); contains only the username
    // Used solely to obtain a new access token — does NOT authorize API calls
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()
                        + jwtProperties.getRefreshTokenExpiry() * 1000))
                .signWith(secretKey())
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        return username.equals(userDetails.getUsername());
    }

    // Parses and verifies the token signature; throws JwtException on failure
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())    // verifies signature
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}