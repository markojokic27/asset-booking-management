package de.bdr.asset.management.core.config.security;

public record RefreshTokenResponseDTO(
        String accessToken,
        String refreshToken
) {}