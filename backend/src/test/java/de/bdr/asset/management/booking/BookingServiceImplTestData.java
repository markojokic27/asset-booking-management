package de.bdr.asset.management.booking;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.assetcategory.AssetCategory;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.user.User;

import static de.bdr.asset.management.booking.TestConstants.*;

public class BookingServiceImplTestData {
    public static User user() {
        User u = new User();
        u.setId(USER_ID);
        u.setName(USER_NAME);
        u.setRole(USER_ROLE);
        return u;
    }

    public static Asset asset() {
        AssetCategory category = new AssetCategory();
        category.setName(CATEGORY_NAME);
        category.setApproval(false);
        Asset a = new Asset();
        a.setId(ASSET_ID);
        a.setName(ASSET_NAME);
        a.setCategory(category);
        a.setStatus(ASSET_STATUS);
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
                USER_SUMMARY,
                ASSET_SUMMARY,
                BookingStatusEnum.ACTIVE,
                START,
                END,
                NOTES_DATA
        );
    }    
}
