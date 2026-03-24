package de.bdr.asset.management.user.department;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record DepartmentDTO(
    Long id,

    @NotNull(message="Name is required")
    DepartmentEnum name,

    Long managerId,

    LocalDateTime createdTimestamp,

    LocalDateTime lastModifiedTimestamp
) {
    
}
