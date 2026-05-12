package de.bdr.asset.management.assetcategory;

import de.bdr.asset.management.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
public class AssetCategory extends BaseEntity {

    /** Name of asset category */
    @Column(nullable=false, length = 100, unique = true)
    private String name;

    /** Description of asset category */
    @Column
    private String description;

    /** Period of booking */
    @Column(nullable=false, length = 50)
    @Enumerated(EnumType.STRING)
    private BookingPeriodEnum bookingPeriod;

    /** Approval for asset category */
    @Column(nullable = false)
    private boolean approval;

}