package de.bdr.asset.management.assetcategory.dto;

import de.bdr.asset.management.assetcategory.BookingPeriodEnum;
import jakarta.validation.constraints.Size;

public record AssetCategoryUpdateRequestDTO(

        @Size(max=100, message="Name cannot exceed 100 characters")
        String name,

        @Size(max=255, message="Description cannot exceed 255 characters")
        String description,

        BookingPeriodEnum bookingPeriod,

        Boolean approval
) {}
