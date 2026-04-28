package de.bdr.asset.management.booking.dto;

import java.time.Instant;

import de.bdr.asset.management.booking.BookingStatusEnum;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

public record BookingUpdateDTO(
        BookingStatusEnum status,

        @Future(message = "Start time has to be in the future")
        Instant bookingStart,

        @Future(message = "End time has to be in the future")
        Instant bookingEnd,

        @Size(max = 1000, message = "Notes cannot exceed 255 characters")
        String notes
) {}
