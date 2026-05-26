package de.bdr.asset.management.user;

import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import org.mapstruct.*;

/** MapStruct data transformation contract bridging user entity, request DTOs and response DTOs. */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    /** Transforms an inbound account creation request into a clean user domain entity instance. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "department", ignore = true)
    User toEntity(UserCreateRequestDTO request);

    /** Projects a live user aggregate instance into an outbound API response view data transfer object. */
    @Mapping(target = "departmentId", source = "department.id")
    UserResponseDTO toResponse(User entity);

    /**
     * Performs a flexible PATCH update.
     * Takes the partial properties provided in the request DTO and merges them directly into your database entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    void updateEntityFromDto(UserUpdateRequestDTO request, @MappingTarget User entity);

}
