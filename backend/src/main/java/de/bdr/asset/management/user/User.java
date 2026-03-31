package de.bdr.asset.management.user;

import de.bdr.asset.management.user.department.Department;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.Instant;


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

    /** Username of user*/
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    /** Family name */
    @Column(nullable = false, length = 100)
    private String surname;

    /** First name */
    @Column(nullable = false, length = 100)
    private String name;

    /** Email of user */
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    /** Password of user */
    @Column(nullable = false, length = 100)
    private String password;

    /** Role of user */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    /** User Status */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    /** ID of department, foreign key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /** Email of manager */
    @Column(nullable = false, length = 100)
    private String managerEmail;

    /** Notes, Additional information's */
    @Column(length = 255)
    private String notes;

    /** User benefits */
    @Column(nullable = false, length = 100)
    private String benefit;

    /** Created at */
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    /** Last Modified at */
    @UpdateTimestamp
    private Instant lastModifiedAt;
}