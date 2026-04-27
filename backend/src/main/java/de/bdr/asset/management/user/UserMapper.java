package de.bdr.asset.management.user;

import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
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
