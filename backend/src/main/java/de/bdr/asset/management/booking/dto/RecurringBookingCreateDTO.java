package de.bdr.asset.management.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;

public record RecurringBookingCreateDTO(

        Long userId,

        @NotNull(message = "Asset ID is required")
        Long assetId,

        @NotEmpty(message = "At least one time slot is required")
        @Size(max = 31, message = "Cannot create more than 31 bookings at once")
        List<@Valid TimeSlotDTO> timeSlots,

        @Size(max = 1000, message = "Notes cannot exceed 255 characters")
        String notes
) {
        @AssertTrue(message = "All time slots must fall within the same calendar month")
        public boolean validateSameMonth() {

                ZoneId zone = ZoneId.of("UTC");

                YearMonth referenceMonth = YearMonth.from(timeSlots.getFirst().bookingStart().atZone(zone));

                for (TimeSlotDTO slot : timeSlots) {

                        YearMonth slotMonth = YearMonth.from(slot.bookingStart().atZone(zone));

                        if (!referenceMonth.equals(slotMonth)) {

                                return false;
                        }
                }
                return true;
        }
}
