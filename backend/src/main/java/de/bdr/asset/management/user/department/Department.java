package de.bdr.asset.management.user.department;

import de.bdr.asset.management.core.domain.BaseEntity;
import de.bdr.asset.management.user.User;
import jakarta.persistence.*;
import lombok.*;

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
public class Department extends BaseEntity {

    /** Name of department */
    @Column(nullable=false, unique = true)
    @Enumerated(EnumType.STRING)
    private DepartmentEnum name;

    /** ID of manager, foreign key */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manager_id", unique = true)
    private User manager;

}
