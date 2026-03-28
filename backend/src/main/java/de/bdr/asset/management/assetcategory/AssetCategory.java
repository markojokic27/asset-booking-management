package de.bdr.asset.management.assetcategory;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
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
    @Column(nullable=false, length = 100, unique = true)
    private String name;

    /** Description of asset category */
    @Column(length = 255)
    private String description;

    /** Period of booking */
    @Column(nullable=false, length = 50)
    @Enumerated(EnumType.STRING)
    private BookingPeriodEnum bookingPeriod;

    /** Approval for asset category */
    @Column(nullable = false)
    private boolean approval;

    /** Created at */
    @CreationTimestamp
    @Column(updatable=false)
    private Instant createdAt;

    /** Last Modified at */
    @UpdateTimestamp
    private Instant lastModifiedAt;
}