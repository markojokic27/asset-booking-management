package de.bdr.asset.management.user.dtos;

import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;
import jakarta.validation.constraints.*;

/**
 * Data transfer contract containing parameters to update an existing user account.
 *
 * @param surname Family name of the account holder.
 * @param name First name of the account holder.
 * @param email Primary unique corporate communication email address.
 * @param role Core security access privileges assigned to the account.
 * @param status Lifecycle operational state of the profile.
 * @param departmentId Unique identity key matching the assigned department.
 * @param managerEmail Corporate email address of the direct structural supervisor.
 * @param notes Optional administrative remarks or additional historical notes.
 * @param benefit Booking benefit plan.
 */
public record UserUpdateRequestDTO(

        @NotBlank(message = "Family name is required")
        @Size(max = 100)
        String surname,

        @NotBlank(message = "First name is required")
        @Size(max = 100)
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email is not in the right format")
        @Size(max = 254)
        String email,

        @NotNull(message = "Role is required")
        UserRoleEnum role,

        @NotNull(message = "Status is required")
        UserStatusEnum status,

        @NotNull(message = "Department ID is required")
        Long departmentId,

        @NotBlank(message = "Manager email is required")
        @Email
        @Size(max = 254)
        String managerEmail,
        
        @Size(max = 1000)
        String notes,

        @NotBlank(message = "Benefit is required")
        @Size(max = 100)
        String benefit
) {}