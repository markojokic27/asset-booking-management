package de.bdr.asset.management.user;

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