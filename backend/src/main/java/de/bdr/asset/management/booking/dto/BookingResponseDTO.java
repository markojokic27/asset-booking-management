package de.bdr.asset.management.booking.dto;

import de.bdr.asset.management.booking.BookingStatusEnum;

import java.time.Instant;

// all expect created/updated at
public record BookingResponseDTO(

        Long id,

        UserSummaryDTO user,

        AssetSummaryDTO asset,

        BookingStatusEnum status,

        Instant bookingStart,

        Instant bookingEnd,

        String notes
) {}
