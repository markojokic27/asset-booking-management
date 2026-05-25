package de.bdr.asset.management.booking;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.core.domain.BaseEntity;
import de.bdr.asset.management.user.User;
import jakarta.persistence.*;
import lombok.*;

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
@ToString(exclude = {"notes"})
public class Booking extends BaseEntity {

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
    private Instant bookingStart;

    /** Booking reservation end */
    @Column(nullable = false)
    private Instant bookingEnd;

    /** Notes, Additional information's */
    @Column
    private String notes;

}
