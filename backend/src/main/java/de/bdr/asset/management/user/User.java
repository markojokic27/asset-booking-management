package de.bdr.asset.management.user;

import de.bdr.asset.management.core.domain.BaseEntity;
import de.bdr.asset.management.user.department.Department;
import jakarta.persistence.*;
import lombok.*;

/** User domain-entity model. */
@Entity
@Table(name = "asset_user")  // cannot name "user" because of conflict with postgres "user" keyword
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password", "notes"})
public class User extends BaseEntity {

    /** Unique login username. */
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    /** Family name. */
    @Column(nullable = false, length = 100)
    private String surname;

    /** First name. */
    @Column(nullable = false, length = 100)
    private String name;

    /** Unique corporate email address of user. */
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    /** Encrypted authentication password hash. */
    @Column(nullable = false, length = 100)
    private String password;

    /** Security and system access authorization role. */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    /** Account lifecycle operational state. */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    /** Associated organizational department. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /** Corporate email address of the direct manager. */
    @Column(nullable = false, length = 100)
    private String managerEmail;

    /** Administrative remarks or additional account metadata. */
    @Column
    private String notes;

    /** User booking benefits. */
    @Column(nullable = false, length = 100)
    private String benefit;
}