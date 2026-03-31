package de.bdr.asset.management.assetcategory;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetCategoryMapper {
    AssetCategory toEntity(AssetCategoryRequestDTO request);
    AssetCategoryResponseDTO toResponse(AssetCategory entity);
}
