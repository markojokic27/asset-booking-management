package de.bdr.asset.management.asset.dtos;

import de.bdr.asset.management.asset.AssetStatusEnum;

public record AssetResponseDTO(

        Long id,

        String name,

        Long categoryId,

        String description,

        String code,

        AssetStatusEnum status,

        String location
) {}
