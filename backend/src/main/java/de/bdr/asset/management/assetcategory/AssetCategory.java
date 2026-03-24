package de.bdr.asset.management.assetcategory;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotBlank(message="Name is required")
    @Size(max=100, message="Name cannot exceed 100 characters")
    @Column(nullable=false)
    private String name;

    @Size(max=255, message="Description cannot exceed 255 characters")
    @Column(columnDefinition="TEXT")
    private String description;

    @NotNull(message="Booking period is required")
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private BookingPeriodEnum bookingPeriod;

    @Column(nullable = false)
    private boolean approval;

    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdTimestamp;

    @UpdateTimestamp
    private LocalDateTime lastModifiedTimestamp;
}