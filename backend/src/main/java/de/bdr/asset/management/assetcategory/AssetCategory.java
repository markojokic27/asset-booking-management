package de.bdr.asset.management.assetcategory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Asset category domain-entity model.
 */
@Entity
@Table(name="asset_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetCategory {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /** Name of asset category */
    @NotBlank(message="Name is required")
    @Size(max=100, message="Name cannot exceed 100 characters")
    @Column(nullable=false)
    private String name;

    /** Description of asset category */
    @Size(max=255, message="Description cannot exceed 255 characters")
    @Column(columnDefinition="TEXT")
    private String description;

    /** Period of booking */
    @NotNull(message="Booking period is required")
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private BookingPeriodEnum bookingPeriod;

    /** Approval for asset category */
    @Column(nullable = false)
    private boolean approval;

    /** Created at */
    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdAt;

    /** Last Modified at */
    @UpdateTimestamp
    private LocalDateTime lastModifiedAt;
}