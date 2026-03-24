package de.bdr.asset.management.assetcategory;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssetCategoryDTO(
        Long id,

        @NotBlank(message="Name is required")
        String name,

        String description,

        @NotNull(message="Booking period is required")
        BookingPeriodEnum bookingPeriod,

        boolean approval,

        LocalDateTime createdTimestamp,

        LocalDateTime lastModifiedTimestamp
) {
}
