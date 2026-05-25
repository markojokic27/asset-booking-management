package de.bdr.asset.management.user.department.dtos;

import de.bdr.asset.management.user.department.DepartmentEnum;
import jakarta.validation.constraints.NotNull;

/**
 * Data transfer contract containing parameters to create or modify a department.
 *
 * @param name Unique organizational department name.
 * @param managerId Optional identity key of the user assigned to manage this department.
 */
public record DepartmentRequestDTO(

        @NotNull(message="Name is required")
        DepartmentEnum name,

        Long managerId
) {}
