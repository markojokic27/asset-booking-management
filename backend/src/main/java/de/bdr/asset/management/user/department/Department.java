package de.bdr.asset.management.user.department;

import de.bdr.asset.management.core.domain.BaseEntity;
import de.bdr.asset.management.user.User;
import jakarta.persistence.*;
import lombok.*;

/** Department domain-entity model. */
@Entity
@Table(name="department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department extends BaseEntity {

    /** Unique organizational department name. */
    @Column(nullable=false, unique = true)
    @Enumerated(EnumType.STRING)
    private DepartmentEnum name;

    /** Designated active manager responsible for supervising this department. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manager_id", unique = true)
    private User manager;
}
