package de.bdr.asset.management.assetcategory;

public record AssetCategoryResponseDTO(

        Long id,

        String name,

        String description,

        BookingPeriodEnum bookingPeriod,

        Boolean approval
) {}
