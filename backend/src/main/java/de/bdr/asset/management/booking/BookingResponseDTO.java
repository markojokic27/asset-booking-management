package de.bdr.asset.management.booking;

import java.time.Instant;

public record BookingResponseDTO(

        Long id,

        Long userId,

        Long assetId,

        BookingStatusEnum status,

        Instant bookingStart,

        Instant bookingEnd,

        String notes
) {}
