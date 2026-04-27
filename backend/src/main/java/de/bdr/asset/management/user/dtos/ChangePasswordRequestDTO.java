package de.bdr.asset.management.user.dtos;

import jakarta.validation.constraints.*;

public record ChangePasswordRequestDTO(
    @NotNull(message = "Current password is required")
    String currentPassword,

    @NotNull(message = "New password is required")
    @Size(min = 8)
    String newPassword

    // Optional: include this for frontend-side validation
    // private String confirmNewPassword;
) {}
