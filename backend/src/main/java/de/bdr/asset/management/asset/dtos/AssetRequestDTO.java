package de.bdr.asset.management.asset.dtos;

import de.bdr.asset.management.asset.AssetStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AssetRequestDTO(

        @NotBlank(message="Name is required")
        @Size(max=100, message="Name cannot exceed 100 characters")
        String name,

        @NotNull(message = "Asset Category ID is required")
        Long categoryId,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        @NotNull(message = "Status is required")
        AssetStatusEnum status,

        @NotBlank(message="Location is required")
        @Size(max=100, message="Location content cannot exceed 255 characters")
        String location
) {}
