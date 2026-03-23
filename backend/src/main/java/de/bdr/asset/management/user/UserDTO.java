package de.bdr.asset.management.user;

import de.bdr.asset.management.user.department.DepartmentEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UserDTO (

        Long id,

        @NotBlank(message = "Username is required")
        String userName,

        @NotBlank(message = "Full name is required")
        String fullName,

        @NotNull(message = "Department is required")
        DepartmentEnum department,

        String room,

        @NotNull(message = "Status is required")
        UserStatusEnum status,

        String notes,

        LocalDateTime createdTimestamp,

        LocalDateTime lastModifiedTimestamp
) {

    /**
     * A static factory method for easy conversion from User to UserDTO
     */
    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getId(),
                user.getUserName(),
                user.getFullName(),
                user.getDepartment(),
                user.getRoom(),
                user.getStatus(),
                user.getNotes(),
                user.getCreatedTimestamp(),
                user.getLastModifiedTimestamp()
        );
    }
}