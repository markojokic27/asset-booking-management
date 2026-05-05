package de.bdr.asset.management.core.security.auth;

public record RefreshTokenResponseDTO(
        String accessToken,
        String refreshToken
) {}