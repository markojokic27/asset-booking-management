package de.bdr.asset.management.user.department;

import de.bdr.asset.management.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private DepartmentEnum name;

    /** ID of manager, foreign key */
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="manager_id", referencedColumnName="id")
    private User manager;

    /** Created at */
    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdAt;

    /** Last Modified at */
    @UpdateTimestamp
    private LocalDateTime lastModifiedAt;
}
