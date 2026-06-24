package de.bdr.asset.management.booking;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import de.bdr.asset.management.asset.AssetStatusEnum;
import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.assetcategory.BookingPeriodEnum;
import de.bdr.asset.management.booking.dto.AssetSummaryDTO;
import de.bdr.asset.management.booking.dto.CategorySummaryDTO;
import de.bdr.asset.management.booking.dto.UserSummaryDTO;
import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;

public final class TestConstants {

    public static final Long USER_ID = 1L;
    public static final Long ASSET_ID = 1L;
    public static final Long BOOKING_ID = 1L;
    public static final Long BOOKING_ID_2 = 2L;
    public static final Long CATEGORY_ID = 10L;
    public static final Long CATEGORY_ID_WITH_APPROVAL = 11L;
    
    public static final String USER_NAME = "Ivan";
    public static final String USER_SURNAME = "Ivić";
    public static final String USER_EMAIL = "ivan.ivic@example.com";
    public static final String MANAGER_EMAIL = "manager@example.com";
    public static final UserRoleEnum USER_ROLE = UserRoleEnum.EMPLOYEE;

    public static final String ASSET_NAME = "Dune";
    public static final AssetStatusEnum ASSET_STATUS = AssetStatusEnum.ACTIVE;
    public static final String ASSET_DESCRIPTION = "Sci-Fi Masterpiece";
    public static final String ASSET_LOCATION = "Shelf A3";

    public static final String CATEGORY_NAME = "Sci-Fi";
    public static final BookingPeriodEnum BOOKING_PERIOD = BookingPeriodEnum.DAY; // Example enum value
    public static final boolean CATEGORY_APPROVAL = false;

    public static final String NOTES_DATA = "Notes";
    public static final String UPDATED_NOTES_DATA = "Notes";
    

    public static final Instant BASE_NOW = Instant.parse("2026-04-01T00:00:00Z");

    public static final Instant START = BASE_NOW.plus(1, ChronoUnit.DAYS);
    public static final Instant END = START.plus(1, ChronoUnit.HOURS);

    public static final UserSummaryDTO USER_SUMMARY = new UserSummaryDTO(
            USER_ID,
            USER_NAME,
            USER_SURNAME,
            USER_EMAIL,
            USER_ROLE,
            MANAGER_EMAIL
    );

    public static final CategorySummaryDTO CATEGORY_SUMMARY = new CategorySummaryDTO(
            CATEGORY_ID,
            CATEGORY_NAME,
            BOOKING_PERIOD,
            CATEGORY_APPROVAL
    );

    public static final AssetSummaryDTO ASSET_SUMMARY = new AssetSummaryDTO(
            ASSET_ID,
            ASSET_NAME,
            CATEGORY_SUMMARY,
            ASSET_STATUS,
            ASSET_DESCRIPTION,
            ASSET_LOCATION
    );

    // Recurring — second time slot starts one day later
    public static final Instant START_2 = START.plus(1, ChronoUnit.DAYS);
    public static final Instant END_2 = START_2.plus(1, ChronoUnit.HOURS);

    public static final CategorySummaryDTO CATEGORY_SUMMARY_WITH_APPROVAL = new CategorySummaryDTO(
            CATEGORY_ID_WITH_APPROVAL,
            "Approval Required",
            BOOKING_PERIOD,
            true
    );

    public static final AssetSummaryDTO ASSET_SUMMARY_WITH_APPROVAL = new AssetSummaryDTO(
            ASSET_ID,
            ASSET_NAME,
            CATEGORY_SUMMARY_WITH_APPROVAL,
            ASSET_STATUS,
            ASSET_DESCRIPTION,
            ASSET_LOCATION
    );

    public static final UserSummaryDTO MANAGER_SUMMARY = new UserSummaryDTO(
            2L,
            "Manager",
            "User",
            "manager@example.com",
            UserRoleEnum.MANAGER,
            "boss@example.com"
    );

    // For users requiring approval:
    public static final String MANAGER_EMAIL_APPROVAL = "approver@example.com";

    public static final List<UserStatusEnum> validUpdateUserStatuses = List.of(
        UserStatusEnum.ACTIVE,
        UserStatusEnum.STUDENT
    );

    private TestConstants() {}
}