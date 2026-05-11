package de.bdr.asset.management.booking.dto;

import de.bdr.asset.management.booking.BookingStatusEnum;

import java.time.Instant;

public record BookingResponseDTO(

        Long id,

        UserSummaryDTO user,

        AssetSummaryDTO asset,

        BookingStatusEnum status,

        Instant bookingStart,

        Instant bookingEnd,

        String notes
) {}
