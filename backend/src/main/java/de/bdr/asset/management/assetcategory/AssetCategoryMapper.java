package de.bdr.asset.management.assetcategory;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Mapper interface to convert between {@link AssetCategory} entities and DTOs. */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true)
)
public interface AssetCategoryMapper {

    /** Maps a request DTO to an asset category entity, ignoring audit fields. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    AssetCategory toEntity(AssetCategoryRequestDTO request);

    /** Maps an asset category entity to a response DTO. */
    AssetCategoryResponseDTO toResponse(AssetCategory entity);
}
