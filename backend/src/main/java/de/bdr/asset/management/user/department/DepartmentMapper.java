package de.bdr.asset.management.user.department;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true)
)
public interface DepartmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "manager", ignore = true)
    Department toEntity(DepartmentRequestDTO request);

    @Mapping(target = "managerId", source = "manager.id")
    DepartmentResponseDTO toResponse(Department entity);
}
