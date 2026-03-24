package de.bdr.asset.management.assetcategory;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record AssetCategoryDTO(
        Long id,

        @NotBlank(message="Name is required")
        String name,

        String description,

        @NotBlank(message="Booking period is required")
        BookingPeriodEnum bookingPeriod,

        boolean approval,

        LocalDateTime createdTimestamp,

        LocalDateTime lastModifiedTimestamp
) {
}
