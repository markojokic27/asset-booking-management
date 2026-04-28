package de.bdr.asset.management.booking;

import static de.bdr.asset.management.booking.TestConstants.ASSET_ID;
import static de.bdr.asset.management.booking.TestConstants.BOOKING_ID;
import static de.bdr.asset.management.booking.TestConstants.END;
import static de.bdr.asset.management.booking.TestConstants.NOTES_DATA;
import static de.bdr.asset.management.booking.TestConstants.START;
import static de.bdr.asset.management.booking.TestConstants.UPDATED_NOTES_DATA;
import static de.bdr.asset.management.booking.TestConstants.USER_ID;
import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;

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
                USER_ID,
                ASSET_ID,
                BookingStatusEnum.ACTIVE,
                START,
                END,
                NOTES_DATA
        );
    }
}