package de.bdr.asset.management.user;

import jakarta.validation.constraints.*;

public record UserDTO (

        Long id,

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores, or hyphens")
        String username,

        @NotBlank(message = "Family name is required")
        @Size(max = 100, message = "Family name cannot exceed 100 characters")
        String surname,

        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name cannot exceed 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email is not in the right format")
        @Size(max = 254, message = "Email cannot exceed 254 characters")
        String email,

        // Here we get clean password, not hash, so we put constraints to its length
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
        String password,

        @NotNull(message = "Role is required")
        UserRoleEnum role,

        @NotNull(message = "Status is required")
        UserStatusEnum status,

        @NotNull(message = "Department ID is required")
        Long departmentId,

        @NotBlank(message = "Manager email is required")
        @Email(message = "Manager email is not in the right format")
        @Size(max = 254, message = "Manager email cannot exceed 254 characters")
        String managerEmail,

        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        String notes
) {
}