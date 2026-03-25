package de.bdr.asset.management.asset;

import de.bdr.asset.management.assetcategory.AssetCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message="Name is required")
    @Size(max=100, message="Name cannot exceed 100 characters")
    @Column(nullable=false)
    private String name;

    /** Description of asset */
    @Size(max=255, message="Description cannot exceed 255 characters")
    @Column(columnDefinition="TEXT")
    private String description;

    /** Asset Status */
    @NotNull(message = "Status is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AssetStatusEnum assetStatus;

    /** ID of asset category, foreign key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private AssetCategory category;

    /** Location of asset */
    @NotBlank(message="Location is required")
    @Size(max=255, message="Location cannot exceed 255 characters")
    @Column(nullable=false)
    private String location;

    /** Created at */
    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdAt;

    /** Last Modified at */
    @UpdateTimestamp
    private LocalDateTime lastModifiedAt;
}
