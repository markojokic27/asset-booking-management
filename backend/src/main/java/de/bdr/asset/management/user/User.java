package de.bdr.asset.management.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User domain-entity model.
 */
@Entity
@Table(name = "asset_user")  // can not put just "user" because of conflict with database user!
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Username, as one retrieved from LDAP */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores, or hyphens")
    @Column(nullable = false, unique = true)
    private String userName;

    /** Full name */
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    @Column(nullable = false)
    private String fullName;

    /** Department name */
    @NotNull(message = "Department is required")
    @Size(max = 100, message = "Department name is too long")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DepartmentEnum department;

    /** Room name */
    @Size(max = 50, message = "Room identifier is too long")
    private String room;

    /** User Status */
    @NotNull(message = "Status is required")
    @Pattern(regexp = "ACTIVE|INACTIVE|STUDENT|LEFT_COMPANY", message = "Status must be ACTIVE, INACTIVE, STUDENT or LEFT_COMPANY")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    /** Notes, Additional information's */
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String notes;

    /** Created at */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdTimestamp;

    /** Last Modified at */
    @UpdateTimestamp
    private LocalDateTime lastModifiedTimestamp;

}