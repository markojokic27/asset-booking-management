package de.bdr.asset.management.booking;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.assetcategory.AssetCategory;

import static de.bdr.asset.management.booking.TestConstants.ASSET_ID;
import static de.bdr.asset.management.booking.TestConstants.ASSET_NAME;
import static de.bdr.asset.management.booking.TestConstants.BOOKING_ID;
import static de.bdr.asset.management.booking.TestConstants.END;
import static de.bdr.asset.management.booking.TestConstants.NOTES_DATA;
import static de.bdr.asset.management.booking.TestConstants.START;
import static de.bdr.asset.management.booking.TestConstants.UPDATED_NOTES_DATA;
import static de.bdr.asset.management.booking.TestConstants.USER_ID;
import static de.bdr.asset.management.booking.TestConstants.USER_NAME;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.user.User;

public class BookingServiceImplTestData {
    public static User user() {
        User u = new User();
        u.setId(USER_ID);
        u.setName(USER_NAME);
        return u;
    }

    public static Asset asset() {
        AssetCategory category = new AssetCategory();
        category.setApproval(false);
        Asset a = new Asset();
        a.setId(ASSET_ID);
        a.setName(ASSET_NAME);
        a.setCategory(category);
        return a;
    }

    public static Booking booking(User user, Asset asset) {
        Booking b = new Booking();
        b.setId(BOOKING_ID);
        b.setUser(user);
        b.setAsset(asset);
        b.setStatus(BookingStatusEnum.ACTIVE);
        b.setBookingStart(START);
        b.setBookingEnd(END);
        b.setNotes(NOTES_DATA);
        return b;
    }

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
