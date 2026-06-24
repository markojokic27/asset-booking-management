package de.bdr.asset.management.booking;

import de.bdr.asset.management.booking.dto.RecurringBookingCreateDTO;
import de.bdr.asset.management.booking.dto.TimeSlotDTO;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecurringBookingCreateDTOTest {

    private static final Instant BASE = Instant.parse("2026-04-01T00:00:00Z");

    @Test
    void shouldPassWhenAllSlotsInSameMonth() {

        var slot1 = new TimeSlotDTO(BASE.plusSeconds(3600), BASE.plusSeconds(7200));
        var slot2 = new TimeSlotDTO(BASE.plusSeconds(86400), BASE.plusSeconds(93600));

        var dto = new RecurringBookingCreateDTO(null, 1L, List.of(slot1, slot2), null);

        assertTrue(dto.validateSameMonth());
    }

    @Test
    void shouldFailWhenSlotsCrossMonthBoundary() {

        Instant april30 = Instant.parse("2026-04-30T00:00:00Z");
        var slot1 = new TimeSlotDTO(april30.plusSeconds(3600), april30.plusSeconds(7200));
        var slot2 = new TimeSlotDTO(april30.plusSeconds(86400), april30.plusSeconds(93600)); // May 1

        var dto = new RecurringBookingCreateDTO(null, 1L, List.of(slot1, slot2), null);

        assertFalse(dto.validateSameMonth());
    }
}
