package de.bdr.asset.management.user.department;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import de.bdr.asset.management.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotNull(message="Name is required")
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private DepartmentEnum name;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User manager;

    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdTimestamp;

    @UpdateTimestamp
    private LocalDateTime lastModifiedTimestamp;
}
