package de.bdr.asset.management.booking;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.booking.dto.RecurringBookingCreateDTO;
import de.bdr.asset.management.booking.dto.TimeSlotDTO;

import java.util.List;

import static de.bdr.asset.management.booking.TestConstants.*;

public final class BookingControllerTestData {

    private BookingControllerTestData() {}

    public static BookingCreateDTO createRequest() {
        return new BookingCreateDTO(
                USER_ID,
                ASSET_ID,
                START,
                END,
                NOTES_DATA
        );
    }

    public static RecurringBookingCreateDTO recurringCreateRequest() {
        return new RecurringBookingCreateDTO(
                USER_ID,
                ASSET_ID,
                List.of(new TimeSlotDTO(START, END), new TimeSlotDTO(START_2, END_2)),
                NOTES_DATA
        );
    }

    public static BookingUpdateDTO updateRequest() {
        return new BookingUpdateDTO(
            null,
            null,
            null,
            UPDATED_NOTES_DATA
        );
    }

    public static BookingResponseDTO response() {
        return new BookingResponseDTO(
                BOOKING_ID,
                USER_SUMMARY,
                ASSET_SUMMARY,
                BookingStatusEnum.PENDING,
                START,
                END,
                NOTES_DATA
        );
    }

    public static BookingResponseDTO approvedResponse() {
        return new BookingResponseDTO(
                BOOKING_ID,
                USER_SUMMARY,
                ASSET_SUMMARY,
                BookingStatusEnum.APPROVED,
                START,
                END,
                NOTES_DATA
        );
    }

    public static BookingResponseDTO rejectedResponse() {
        return new BookingResponseDTO(
                BOOKING_ID,
                USER_SUMMARY,
                ASSET_SUMMARY,
                BookingStatusEnum.REJECTED,
                START,
                END,
                NOTES_DATA
        );
    }
}