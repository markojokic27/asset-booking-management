package de.bdr.asset.management.user.login;

public record LoginResponseDTO(
        String accessToken,
        String refreshToken,
        String username,
        String role
) {}