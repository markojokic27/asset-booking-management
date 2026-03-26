package de.bdr.asset.management.asset;

import jakarta.validation.constraints.*;

public record AssetDTO(

        Long id,

        @NotBlank(message="Name is required")
        @Size(max=100, message="Name cannot exceed 100 characters")
        String name,

        @NotNull(message = "Asset Category ID is required")
        Long categoryId,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        @NotBlank(message="QR code is required")
        @Size(max=2000, message="QR code content cannot exceed 2000 characters")
        String code,

        @NotNull(message = "Status is required")
        AssetStatusEnum status,

        @NotBlank(message="Location is required")
        @Size(max=255, message="Location content cannot exceed 255 characters")
        String location
) {
}
