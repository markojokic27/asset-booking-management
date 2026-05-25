package de.bdr.asset.management.user.dtos;

import jakarta.validation.constraints.*;

/**
 * Data transfer contract containing credentials required to modify an authentication password.
 *
 * @param currentPassword Existing raw plain text password used for identity verification.
 * @param newPassword New raw plain text password string to replace the old credentials.
 */
public record ChangePasswordRequestDTO(
    @NotNull(message = "Current password is required")
    String currentPassword,

    @NotNull(message = "New password is required")
    @Size(min = 8)
    String newPassword
) {}
