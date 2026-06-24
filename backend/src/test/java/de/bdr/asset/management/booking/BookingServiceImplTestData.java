package de.bdr.asset.management.booking;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.assetcategory.AssetCategory;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.booking.dto.RecurringBookingCreateDTO;
import de.bdr.asset.management.booking.dto.TimeSlotDTO;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRoleEnum;

import java.util.List;

import static de.bdr.asset.management.booking.TestConstants.*;

public class BookingServiceImplTestData {
    public static User user() {
        User u = new User();
        u.setId(USER_ID);
        u.setName(USER_NAME);
        u.setSurname(USER_SURNAME);
        u.setEmail(USER_EMAIL);
        u.setRole(USER_ROLE);
        u.setManagerEmail(MANAGER_EMAIL);
        return u;
    }

    public static User managerUser() {
        User u = new User();
        u.setId(2L);
        u.setName("Manager");
        u.setSurname("User");
        u.setEmail("manager@example.com");
        u.setRole(UserRoleEnum.MANAGER);
        u.setManagerEmail("boss@example.com");
        return u;
    }

    public static Asset asset() {
        AssetCategory category = new AssetCategory();
        category.setId(CATEGORY_ID);
        category.setName(CATEGORY_NAME);
        category.setBookingPeriod(BOOKING_PERIOD);
        category.setApproval(CATEGORY_APPROVAL);

        Asset a = new Asset();
        a.setId(ASSET_ID);
        a.setName(ASSET_NAME);
        a.setDescription(ASSET_DESCRIPTION);
        a.setLocation(ASSET_LOCATION);
        a.setCategory(category);
        a.setStatus(ASSET_STATUS);

        return a;
    }

    public static Asset assetWithApprovalRequired() {
        AssetCategory category = new AssetCategory();
        category.setId(CATEGORY_ID_WITH_APPROVAL);
        category.setName("Approval Required");
        category.setBookingPeriod(BOOKING_PERIOD);
        category.setApproval(true);

        Asset a = new Asset();
        a.setId(ASSET_ID);
        a.setName(ASSET_NAME);
        a.setDescription(ASSET_DESCRIPTION);
        a.setLocation(ASSET_LOCATION);
        a.setCategory(category);
        a.setStatus(ASSET_STATUS);

        return a;
    }

    public static Booking booking(User user, Asset asset) {
        Booking b = new Booking();
        b.setId(BOOKING_ID);
        b.setUser(user);
        b.setAsset(asset);
        b.setStatus(BookingStatusEnum.PENDING);
        b.setBookingStart(START);
        b.setBookingEnd(END);
        b.setNotes(NOTES_DATA);
        return b;
    }

    public static Booking approvedBooking(User user, Asset asset) {
        Booking b = booking(user, asset);
        b.setStatus(BookingStatusEnum.APPROVED);
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

    // ── Recurring helpers ──────────────────────────────────────────

    public static TimeSlotDTO timeSlot() {
        return new TimeSlotDTO(START, END);
    }

    public static TimeSlotDTO timeSlot2() {
        return new TimeSlotDTO(START_2, END_2);
    }

    public static RecurringBookingCreateDTO recurringCreateRequest() {
        return new RecurringBookingCreateDTO(
                USER_ID,
                ASSET_ID,
                List.of(timeSlot(), timeSlot2()),
                NOTES_DATA
        );
    }
}
