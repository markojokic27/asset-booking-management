package de.bdr.asset.management.user;

/** User operational lifecycle status. */
public enum UserStatusEnum {

    /** Account is active and has full system access. */
    ACTIVE,

    /** Account is temporarily paused due to extended leave. */
    INACTIVE,

    /** Intern or trainee completing a temporary practice placement. */
    STUDENT,

    /** Employee departed this fiscal year; preserved for annual reporting. */
    LEFT_COMPANY,

    /** Account is soft-deleted and hidden from general system views. */
    DELETED
}
