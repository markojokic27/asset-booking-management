package de.bdr.asset.management.asset;

import de.bdr.asset.management.asset.dtos.AssetRequestDTO;
import de.bdr.asset.management.asset.dtos.AssetResponseDTO;
import de.bdr.asset.management.asset.dtos.AssetUpdateRequestDTO;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AssetMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    Asset toEntity(AssetRequestDTO request);
    
    @Mapping(target = "categoryId", source = "category.id")
    AssetResponseDTO toResponse(Asset entity);

    /**
     * Performs a flexible PATCH update on an existing asset.
     * Takes the partial fields from the update request DTO and merges them directly into the entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // Ignored here to resolve safely via DB lookup in service
    void updateEntityFromDto(AssetUpdateRequestDTO request, @MappingTarget Asset entity);
}
