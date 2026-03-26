package de.bdr.asset.management.asset;

import de.bdr.asset.management.assetcategory.AssetCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
    @Column(columnDefinition="TEXT")
    private String description;

    /** QR code of asset */
    @Column(nullable = false, unique = true, length = 2000)
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
    private LocalDateTime createdAt;

    /** Last Modified at */
    @UpdateTimestamp
    private LocalDateTime lastModifiedAt;
}
