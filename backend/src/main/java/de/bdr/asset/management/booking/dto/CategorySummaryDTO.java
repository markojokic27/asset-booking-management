package de.bdr.asset.management.booking.dto;

import de.bdr.asset.management.assetcategory.BookingPeriodEnum;

public record CategorySummaryDTO(
        Long id,
        String name,
        BookingPeriodEnum bookingPeriod,
        boolean approval
)
{}
