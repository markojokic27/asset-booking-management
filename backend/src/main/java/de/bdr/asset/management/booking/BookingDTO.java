package de.bdr.asset.management.booking;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record BookingDTO(

        Long id,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Asset ID is required")
        Long assetId,

        @NotNull(message = "Status is required")
        BookingStatusEnum status,

        @NotNull(message = "Start time is required")
        LocalDateTime bookingStartTime,

        @NotNull(message = "End time is required")
        LocalDateTime bookingEndTime,

        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        String notes
) {
}
