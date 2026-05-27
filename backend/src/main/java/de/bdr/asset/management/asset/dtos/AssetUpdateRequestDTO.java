package de.bdr.asset.management.asset.dtos;

import de.bdr.asset.management.asset.AssetStatusEnum;
import jakarta.validation.constraints.Size;

public record AssetUpdateRequestDTO(

        @Size(max=100, message="Name cannot exceed 100 characters")
        String name,

        Long categoryId,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        AssetStatusEnum status,

        @Size(max=100, message="Location content cannot exceed 255 characters")
        String location
) {}