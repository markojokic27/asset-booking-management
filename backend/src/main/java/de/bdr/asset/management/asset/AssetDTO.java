package de.bdr.asset.management.asset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AssetDTO(
        Long id,

        @NotBlank(message="Name is required")
        String name,

        @NotNull(message = "Category ID is required")
        Long categoryId,

        @NotNull(message = "Status is required")
        AssetStatusEnum assetStatus,

        String description,

        @NotBlank(message="Location is required")
        String location,

        LocalDateTime createdTimestamp,

        LocalDateTime lastModifiedTimestamp



        ) {
}
