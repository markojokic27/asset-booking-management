package de.bdr.asset.management.user.department.dtos;

import de.bdr.asset.management.user.department.DepartmentEnum;
import jakarta.validation.constraints.NotNull;

/**
 * Data transfer contract containing optional parameters to partially update an existing department.
 * <p>
 * This DTO supports selective field updates (PATCH). Fields that are omitted or passed as
 * {@code null} will remain unchanged in the underlying database record.
 * </p>
 *
 * @param name Optional unique organizational department name.
 * @param managerId Optional identity key of the user assigned to manage this department.
 */
public record DepartmentUpdateRequestDTO(

        DepartmentEnum name,

        Long managerId
) {}
