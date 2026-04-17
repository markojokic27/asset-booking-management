package de.bdr.asset.management.core.config.security;

public record LoginResponseDTO(
        String accessToken,
        String refreshToken
) {}