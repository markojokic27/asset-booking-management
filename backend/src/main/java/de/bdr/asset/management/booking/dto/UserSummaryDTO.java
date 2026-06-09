package de.bdr.asset.management.booking.dto;

import de.bdr.asset.management.user.UserRoleEnum;

public record UserSummaryDTO(
        Long id,
        String name,
        String surname,
        String email,
        UserRoleEnum role,
        String managerEmail
) {}
