package de.bdr.asset.management.user.department;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** MapStruct data transformation contract bridging department entity, request DTOs and response DTOs. */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true)
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
}
