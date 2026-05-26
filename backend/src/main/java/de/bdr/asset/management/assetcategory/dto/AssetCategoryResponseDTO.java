package de.bdr.asset.management.assetcategory.dto;

import de.bdr.asset.management.assetcategory.BookingPeriodEnum;

public record AssetCategoryResponseDTO(

        Long id,

        String name,

        String description,

        BookingPeriodEnum bookingPeriod,

        Boolean approval
) {}
