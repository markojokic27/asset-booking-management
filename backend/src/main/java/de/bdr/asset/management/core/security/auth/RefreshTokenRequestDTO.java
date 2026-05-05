package de.bdr.asset.management.core.security.auth;

public record RefreshTokenRequestDTO(
        String refreshToken
) {}