package de.bdr.asset.management.asset;

import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

/**
 * Asset domain-entity model.
 */
@Entity
@Table(name = "asset")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FilterDef(name = "softDeleteFilter", parameters = @ParamDef(name = "deletedStatus", type = String.class))
@Filter(name = "softDeleteFilter", condition = "status <> :deletedStatus")
public class Asset extends BaseEntity {

    /** Name of asset */
    @Column(nullable = false, length = 100)
    private String name;

    /** ID of asset category, foreign key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private AssetCategory category;

    /** Description of asset */
    @Column
    private String description;

    /** QR code of asset */
    @Column(unique = true, length = 2000)
    private String code;

    /** Asset Status */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AssetStatusEnum status;

    /** Location of asset */
    @Column(nullable = false)
    private String location;

}
