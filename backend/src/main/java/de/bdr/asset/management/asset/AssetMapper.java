package de.bdr.asset.management.asset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    Asset toEntity(AssetRequestDTO request);
    
    @Mapping(target = "categoryId", source = "category.id")
    AssetResponseDTO toResponse(Asset entity);
}
