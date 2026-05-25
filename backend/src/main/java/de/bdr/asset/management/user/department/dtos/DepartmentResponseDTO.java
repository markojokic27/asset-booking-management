package de.bdr.asset.management.user.department.dtos;

import de.bdr.asset.management.user.department.DepartmentEnum;

/**
 * Data view representing a summary of an organizational department.
 *
 * @param id Unique database identity index.
 * @param name Unique organizational department name.
 * @param managerId Optional identity key of the managing user assigned to this department.
 */
public record DepartmentResponseDTO(

        Long id,

        DepartmentEnum name,

        Long managerId
) {}
