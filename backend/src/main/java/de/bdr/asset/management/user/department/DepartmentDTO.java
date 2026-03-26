package de.bdr.asset.management.user.department;

import jakarta.validation.constraints.*;

public record DepartmentDTO(

        Long id,

        @NotNull(message="Name is required")
        String name,

        @NotNull(message = "Manager ID is required")
        Long managerId
) {
}
