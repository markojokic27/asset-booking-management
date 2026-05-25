package de.bdr.asset.management.user.department.dtos;

import de.bdr.asset.management.user.department.DepartmentEnum;
import jakarta.validation.constraints.NotNull;

public record DepartmentRequestDTO(

        @NotNull(message="Name is required")
        DepartmentEnum name,

        Long managerId
) {}
