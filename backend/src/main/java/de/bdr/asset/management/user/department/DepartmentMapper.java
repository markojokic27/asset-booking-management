package de.bdr.asset.management.user.department;

import de.bdr.asset.management.user.department.dtos.DepartmentRequestDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentResponseDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentUpdateRequestDTO;
import org.mapstruct.*;

/** MapStruct data transformation contract bridging department entity, request DTOs and response DTOs. */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DepartmentMapper {

    /** Transforms an inbound creation request into a clean department domain entity instance. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "manager", ignore = true)
    Department toEntity(DepartmentRequestDTO request);

    /** Projects a live department entity instance into an outbound API response view data transfer object. */
    @Mapping(target = "managerId", source = "manager.id")
    DepartmentResponseDTO toResponse(Department entity);

    /**
     * Performs a flexible PATCH update on an existing department.
     * Takes the partial fields from the update request DTO and merges them directly into the entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manager", ignore = true) // Handled manually in the service layer
    void updateEntityFromDto(DepartmentUpdateRequestDTO request, @MappingTarget Department entity);
}
