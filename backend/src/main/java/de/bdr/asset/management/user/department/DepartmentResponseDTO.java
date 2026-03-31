package de.bdr.asset.management.user.department;

public record DepartmentResponseDTO(

        Long id,

        DepartmentEnum name,

        Long managerId
) {}
