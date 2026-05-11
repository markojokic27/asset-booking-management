package de.bdr.asset.management.booking;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.user.User;

import static de.bdr.asset.management.booking.TestConstants.*;

public class BookingMapperTestData {

    private BookingMapperTestData() {};

    public static Booking buildBookingWithNullRelations() {
        Booking b = new Booking();
        b.setId(BOOKING_ID);
        b.setBookingStart(START);
        b.setBookingEnd(END);
        b.setStatus(BookingStatusEnum.ACTIVE);
        b.setNotes(NOTES_DATA);
        return b;
    }

    public static Booking buildBookingWithRelations() {
        Booking b = buildBookingWithNullRelations();

        User user = new User();
        user.setId(USER_ID);
        user.setUsername(USER_NAME);
        user.setRole(USER_ROLE);

        AssetCategory category = new AssetCategory();
        category.setName(CATEGORY_NAME);

        Asset asset = new Asset();
        asset.setName(ASSET_NAME);
        asset.setStatus(ASSET_STATUS);
        asset.setCategory(category);

        b.setUser(user);
        b.setAsset(asset);
        return b;
    }

    public static BookingCreateDTO createRequest(boolean notes) {
        return new BookingCreateDTO(
                USER_ID,
                ASSET_ID,
                START,
                END,
                notes ? NOTES_DATA : null
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
