package de.bdr.asset.management.booking;

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
        Instant bookingStartTime,

        @NotNull(message = "End time is required")
        Instant bookingEndTime,

        @Size(max = 1000, message = "Notes cannot exceed 255 characters")
        String notes
) {}
