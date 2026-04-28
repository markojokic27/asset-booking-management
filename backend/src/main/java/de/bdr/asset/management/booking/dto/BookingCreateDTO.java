package de.bdr.asset.management.booking.dto;

import java.time.Instant;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookingCreateDTO(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Asset ID is required")
        Long assetId,

        @NotNull(message = "Start time is required")
        @Future(message = "Start time has to be in the future")
        Instant bookingStart,

        @NotNull(message = "End time is required")
        @Future(message = "End time has to be in the future")
        Instant bookingEnd,

        @Size(max = 1000, message = "Notes cannot exceed 255 characters")
        String notes
) {}
