package de.bdr.asset.management.user.dtos;

import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;

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