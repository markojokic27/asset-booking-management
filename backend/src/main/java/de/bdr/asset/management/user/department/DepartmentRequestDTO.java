package de.bdr.asset.management.user.department;

import jakarta.validation.constraints.NotNull;

public record DepartmentRequestDTO(

        @NotNull(message="Name is required")
        DepartmentEnum name,

        @NotNull(message = "Manager ID is required")
        Long managerId
) {}
