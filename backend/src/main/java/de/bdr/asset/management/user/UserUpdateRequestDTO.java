package de.bdr.asset.management.user;

import jakarta.validation.constraints.*;

public record UserUpdateRequestDTO(

        @NotNull(message = "Status is required")
        UserStatusEnum status,

        @Size(max = 1000)
        String notes,

        @NotBlank(message = "Benefit is required")
        @Size(max = 100)
        String benefit
) {}