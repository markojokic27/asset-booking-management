package de.bdr.asset.management.user;

/**
 * User Status enumeration.
 */
public enum UserStatusEnum {
    ACTIVE,
    INACTIVE,     // like on maternity leave
    STUDENT,      // student on internship or practice
    LEFT_COMPANY, // left company this year, but we keep it just for reporting (in current year)
    DELETED
}
