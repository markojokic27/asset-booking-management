package de.bdr.asset.management.user.department;

import de.bdr.asset.management.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.Instant;

/**
 * Department domain-entity model.
 */
@Entity
@Table(name="department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /** Name of department */
    @Column(nullable=false, unique = true)
    @Enumerated(EnumType.STRING)
    private DepartmentEnum name;

    /** ID of manager, foreign key */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manager_id", unique = true)
    private User manager;

    /** Created at */
    @CreationTimestamp
    @Column(updatable=false)
    private Instant createdAt;

    /** Last Modified at */
    @UpdateTimestamp
    private Instant lastModifiedAt;
}
