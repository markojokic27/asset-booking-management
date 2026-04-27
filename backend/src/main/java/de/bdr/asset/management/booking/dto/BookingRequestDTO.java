package de.bdr.asset.management.booking.dto;

import de.bdr.asset.management.booking.BookingStatusEnum;
import jakarta.validation.constraints.*;

import java.time.Instant;

public record BookingRequestDTO(

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Asset ID is required")
        Long assetId,

        @NotNull(message = "Status is required")
        BookingStatusEnum status,

        @NotNull(message = "Start time is required")
        Instant bookingStart,

        @NotNull(message = "End time is required")
        Instant bookingEnd,

        @Size(max = 1000, message = "Notes cannot exceed 255 characters")
        String notes
) {}
