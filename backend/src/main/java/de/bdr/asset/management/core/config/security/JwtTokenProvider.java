package de.bdr.asset.management.core.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

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

    // Access token: short-lived (15 min); contains roles for authorization decisions
    public String generateAccessToken(UserDetails userDetails)
    {
        Long userId = ((CustomUserDetails) userDetails).getId();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("userId", userId)
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())                         // ["ROLE_EMPLOYEE"] or ["ROLE_ADMIN"]
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
        // Both conditions must hold: username matches AND token is not expired
        return username.equals(userDetails.getUsername())
                && !parseClaims(token).getExpiration().before(new Date());
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