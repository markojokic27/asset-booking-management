package de.bdr.asset.management.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record TimeSlotDTO(

        @NotNull(message = "Start time is required")
        @Future(message = "Start time has to be in the future")
        Instant bookingStart,

        @NotNull(message = "End time is required")
        @Future(message = "End time has to be in the future")
        Instant bookingEnd
) {
        @AssertTrue(message = "Booking end time must be after the start time")
        public boolean validateBookingPeriod() {
            return bookingStart.isBefore(bookingEnd);
        }
}
