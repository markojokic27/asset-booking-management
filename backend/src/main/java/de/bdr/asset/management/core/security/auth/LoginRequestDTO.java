package de.bdr.asset.management.core.security.auth;

import jakarta.validation.constraints.*;

public record LoginRequestDTO(

        @NotNull(message = "Username is required")
        @Size(min = 3, max = 50)
        String username,

        @NotNull(message = "Password is required")
        @Size(min = 8, max = 50)
        String password
) {}