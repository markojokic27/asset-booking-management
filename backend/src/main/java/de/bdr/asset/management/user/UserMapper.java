package de.bdr.asset.management.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "department", ignore = true)
    User toEntity(UserCreateRequestDTO request);

    @Mapping(target = "departmentId", source = "department.id")
    UserResponseDTO toResponse(User entity);
}
