package de.bdr.asset.management.booking;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Booking domain-entity model.
 */
@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of user, foreign key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** ID of asset, foreign key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    /** Booking Status */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status;

    /** Booking reservation start */
    @Column(nullable = false)
    private Instant bookingStartTime;

    /** Booking reservation end */
    @Column(nullable = false)
    private Instant bookingEndTime;

    /** Notes, Additional information's */
    @Column(length = 255)
    private String notes;

    /** Created at */
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    /** Last Modified at */
    @UpdateTimestamp
    private Instant lastModifiedAt;
}
