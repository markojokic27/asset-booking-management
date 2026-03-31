package de.bdr.asset.management.asset;

import de.bdr.asset.management.assetcategory.AssetCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.Instant;

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
public class Asset {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /** Name of asset */
    @Column(nullable = false, length = 100)
    private String name;

    /** ID of asset category, foreign key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private AssetCategory category;

    /** Description of asset */
    @Column(length = 255)
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

    /** Created at */
    @CreationTimestamp
    @Column(updatable=false)
    private Instant createdAt;

    /** Last Modified at */
    @UpdateTimestamp
    private Instant lastModifiedAt;
}
