package de.bdr.asset.management.booking;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.user.User;

import static de.bdr.asset.management.booking.TestConstants.*;

public class BookingMapperTestData {

    public static Booking buildBookingWithNullRelations() {
        Booking b = new Booking();
        b.setId(BOOKING_ID);
        b.setBookingStart(START);
        b.setBookingEnd(END);
        b.setStatus(BookingStatusEnum.PENDING);
        b.setNotes(NOTES_DATA);
        return b;
    }

    public static Booking buildBookingWithRelations() {
        Booking b = buildBookingWithNullRelations();

        User user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        user.setSurname(USER_SURNAME);
        user.setEmail(USER_EMAIL);
        user.setRole(USER_ROLE);
        user.setManagerEmail(MANAGER_EMAIL);

        AssetCategory category = new AssetCategory();
        category.setId(CATEGORY_ID);
        category.setName(CATEGORY_NAME);
        category.setBookingPeriod(BOOKING_PERIOD);
        category.setApproval(CATEGORY_APPROVAL);

        Asset asset = new Asset();
        asset.setId(ASSET_ID);
        asset.setName(ASSET_NAME);
        asset.setStatus(ASSET_STATUS);
        asset.setDescription(ASSET_DESCRIPTION);
        asset.setLocation(ASSET_LOCATION);
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
                BookingStatusEnum.PENDING,
                START,
                END,
                NOTES_DATA
        );
    }
}
