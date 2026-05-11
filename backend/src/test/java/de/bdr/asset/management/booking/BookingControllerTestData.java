package de.bdr.asset.management.booking;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;

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
                BookingStatusEnum.ACTIVE,
                START,
                END,
                NOTES_DATA
        );
    }
}