package de.bdr.asset.management.assetcategory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    AssetCategory toEntity(AssetCategoryRequestDTO request);
    
    AssetCategoryResponseDTO toResponse(AssetCategory entity);
}
