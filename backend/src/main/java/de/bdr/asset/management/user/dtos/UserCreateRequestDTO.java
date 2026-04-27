package de.bdr.asset.management.user.dtos;

import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;
import jakarta.validation.constraints.*;

public record UserCreateRequestDTO(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores, or hyphens")
        String username,

        @NotBlank(message = "Family name is required")
        @Size(max = 100)
        String surname,

        @NotBlank(message = "First name is required")
        @Size(max = 100)
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email is not in the right format")
        @Size(max = 254)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 50)
        String password,

        @NotNull(message = "Role is required")
        UserRoleEnum role,

        @NotNull(message = "Status is required")
        UserStatusEnum status,

        @NotNull(message = "Department ID is required")
        Long departmentId,

        @NotBlank(message = "Manager email is required")
        @Email
        @Size(max = 254)
        String managerEmail,

        @Size(max = 1000)
        String notes,

        @NotBlank(message = "Benefit is required")
        @Size(max = 100)
        String benefit
) {}