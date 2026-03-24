package de.bdr.asset.management.booking;

import jakarta.validation.constraints.NotNull;

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
        LocalDateTime startTime,

        @NotNull(message = "End time is required")
        LocalDateTime endTime,

        String notes,

        LocalDateTime createdTimestamp,

        LocalDateTime lastModifiedTimestamp
) {
}
