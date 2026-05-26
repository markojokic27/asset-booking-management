package de.bdr.asset.management.user.dtos;

import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;
import jakarta.validation.constraints.*;

/**
 * Data transfer contract containing optional parameters to partially update an existing user account.
 * <p>
 * This DTO supports selective field updates (PATCH). Fields that are omitted or passed as
 * {@code null} will remain unchanged in the underlying database record.
 * </p>
 *
 * @param surname Optional family name of the account holder.
 * @param name Optional first name of the account holder.
 * @param email Optional primary unique corporate communication email address.
 * @param role Optional core security access privileges assigned to the account.
 * @param status Optional lifecycle operational state of the profile.
 * @param departmentId Optional unique identity key matching the assigned organizational department.
 * @param managerEmail Optional corporate email address of the direct structural supervisor.
 * @param notes Optional administrative remarks or additional historical notes.
 * @param benefit Optional booking benefit plan assigned to the user.
 */
public record UserUpdateRequestDTO(

        @Size(max = 100)
        String surname,

        @Size(max = 100)
        String name,

        @Email
        @Size(max = 254)
        String email,

        UserRoleEnum role,

        UserStatusEnum status,

        Long departmentId,

        @Email
        @Size(max = 254)
        String managerEmail,
        
        @Size(max = 1000)
        String notes,

        @Size(max = 100)
        String benefit
) {}