package de.bdr.asset.management.core.security.auth;

public record LoginResponseDTO(
        String accessToken,
        String refreshToken
) {}