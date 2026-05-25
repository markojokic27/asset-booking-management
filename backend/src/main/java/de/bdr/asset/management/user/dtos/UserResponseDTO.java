package de.bdr.asset.management.user.dtos;

import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;

/**
 * Data view representing a user profile summary.
 *
 * @param id Unique database identity index.
 * @param username Unique login username.
 * @param surname Family name.
 * @param name First name.
 * @param email Primary unique corporate email address.
 * @param role Security access privileges assigned to the account.
 * @param status Lifecycle operational state of the profile.
 * @param departmentId Unique identity key of the assigned department.
 * @param managerEmail Corporate email address of the direct supervisor.
 * @param notes Administrative remarks or account metadata.
 * @param benefit Booking benefit plan.
 */
public record UserResponseDTO(

        Long id,

        String username,

        String surname,

        String name,

        String email,

        UserRoleEnum role,

        UserStatusEnum status,

        Long departmentId,

        String managerEmail,

        String notes,

        String benefit
) {}