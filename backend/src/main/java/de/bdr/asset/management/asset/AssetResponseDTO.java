package de.bdr.asset.management.asset;

public record AssetResponseDTO(

        Long id,

        String name,

        Long categoryId,

        String description,

        String code,

        AssetStatusEnum status,

        String location
) {}
