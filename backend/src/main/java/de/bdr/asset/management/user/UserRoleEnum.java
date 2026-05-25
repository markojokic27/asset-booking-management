package de.bdr.asset.management.user;

/** User system authorization and security role. */
public enum UserRoleEnum {

    /** Standard system user with access to general employee self-service features. */
    EMPLOYEE,

    /** Department supervisor authorized to approve asset booking requests for restricted categories. */
    MANAGER,

    /** System administrator with unrestricted operational permissions across all modules. */
    ADMIN
}
