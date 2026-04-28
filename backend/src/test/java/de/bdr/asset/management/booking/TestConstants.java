package de.bdr.asset.management.booking;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import de.bdr.asset.management.user.UserStatusEnum;

public final class TestConstants {

    public static final Long USER_ID = 1L;
    public static final Long ASSET_ID = 1L;
    public static final Long BOOKING_ID = 1L;
    
    public static final String USER_NAME = "Ivan Ivić";
    public static final String ASSET_NAME = "Dune";

    public static final String NOTES_DATA = "Notes";
    public static final String UPDATED_NOTES_DATA = "Notes";
    

    public static final Instant BASE_NOW = Instant.parse("2026-04-01T00:00:00Z");

    public static final Instant START = BASE_NOW.plus(1, ChronoUnit.DAYS);
    public static final Instant END = START.plus(1, ChronoUnit.HOURS);

    public static final List<UserStatusEnum> validUpdateUserStatuses = List.of(
        UserStatusEnum.ACTIVE,
        UserStatusEnum.STUDENT
    );

    private TestConstants() {}
}