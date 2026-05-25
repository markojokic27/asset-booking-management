package de.bdr.asset.management.user.department.dtos;

import de.bdr.asset.management.user.department.DepartmentEnum;

public record DepartmentResponseDTO(

        Long id,

        DepartmentEnum name,

        Long managerId
) {}
