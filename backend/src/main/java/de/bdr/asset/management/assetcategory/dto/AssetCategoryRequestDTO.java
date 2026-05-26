package de.bdr.asset.management.assetcategory.dto;

import de.bdr.asset.management.assetcategory.BookingPeriodEnum;
import jakarta.validation.constraints.*;

public record AssetCategoryRequestDTO(

        @NotBlank(message="Name is required")
        @Size(max=100, message="Name cannot exceed 100 characters")
        String name,

        @Size(max=255, message="Description cannot exceed 255 characters")
        String description,

        @NotNull(message="Booking period is required")
        BookingPeriodEnum bookingPeriod,

        @NotNull(message="Approval is required")
        Boolean approval
) {}
