package de.bdr.asset.management.assetcategory;

import de.bdr.asset.management.assetcategory.dto.AssetCategoryRequestDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryResponseDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryUpdateRequestDTO;
import org.mapstruct.*;

/** Mapper interface to convert between {@link AssetCategory} entities and DTOs. */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AssetCategoryMapper {

    /** Maps a request DTO to an asset category entity, ignoring audit fields. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    AssetCategory toEntity(AssetCategoryRequestDTO request);

    /** Maps an asset category entity to a response DTO. */
    AssetCategoryResponseDTO toResponse(AssetCategory entity);

    /**
     * Performs a flexible PATCH update on an existing asset category.
     * Takes the partial fields from the update request DTO and merges them directly into the entity.
     */
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AssetCategoryUpdateRequestDTO request, @MappingTarget AssetCategory entity);
}
