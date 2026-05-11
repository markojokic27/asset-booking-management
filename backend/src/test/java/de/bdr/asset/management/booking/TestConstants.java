package de.bdr.asset.management.booking;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import de.bdr.asset.management.asset.AssetStatusEnum;
import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.booking.dto.AssetSummaryDTO;
import de.bdr.asset.management.booking.dto.UserSummaryDTO;
import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;

public final class TestConstants {

    public static final Long USER_ID = 1L;
    public static final Long ASSET_ID = 1L;
    public static final Long BOOKING_ID = 1L;
    
    public static final String USER_NAME = "Ivan Ivić";
    public static final UserRoleEnum USER_ROLE = UserRoleEnum.EMPLOYEE;

    public static final String ASSET_NAME = "Dune";
    public static final String CATEGORY_NAME = "Sci-Fi";
    public static final AssetStatusEnum ASSET_STATUS = AssetStatusEnum.ACTIVE;

    public static final String NOTES_DATA = "Notes";
    public static final String UPDATED_NOTES_DATA = "Notes";
    

    public static final Instant BASE_NOW = Instant.parse("2026-04-01T00:00:00Z");

    public static final Instant START = BASE_NOW.plus(1, ChronoUnit.DAYS);
    public static final Instant END = START.plus(1, ChronoUnit.HOURS);

    public static final UserSummaryDTO USER_SUMMARY = new UserSummaryDTO(
            USER_ID,
            USER_NAME,
            USER_ROLE
    );

    public static final AssetSummaryDTO ASSET_SUMMARY = new AssetSummaryDTO(
            ASSET_NAME,
            CATEGORY_NAME,
            ASSET_STATUS
    );

    public static final List<UserStatusEnum> validUpdateUserStatuses = List.of(
        UserStatusEnum.ACTIVE,
        UserStatusEnum.STUDENT
    );

    private TestConstants() {}
}