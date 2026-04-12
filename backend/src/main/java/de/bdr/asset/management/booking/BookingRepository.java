package de.bdr.asset.management.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

/**
 * JPA Booking Repository
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT COUNT(*) FROM booking " +
                   "WHERE asset_id = :assetId " +
                   "AND booking_end > :newStart " +
                   "AND booking_start < :newEnd " +
                   "AND status IN ('ACTIVE', 'APPROVED', 'PENDING')",
           nativeQuery = true)
    int countOverlappingBookings(
            @Param("assetId") Long assetId,
            @Param("newStart") Instant newStart,
            @Param("newEnd") Instant newEnd
    );

    @Query(value = "SELECT COUNT(*) FROM booking " +
                   "WHERE asset_id = :assetId " +
                   "AND id != :bookingId " +
                   "AND booking_end > :newStart " +
                   "AND booking_start < :newEnd " +
                   "AND status IN ('ACTIVE', 'APPROVED', 'PENDING')",
           nativeQuery = true)
    int countOverlappingBookingsForUpdate(
            @Param("assetId") Long assetId,
            @Param("newStart") Instant newStart,
            @Param("newEnd") Instant newEnd,
            @Param("bookingId") Long bookingId
    );

    @EntityGraph(attributePaths = {"user", "asset"})
    Optional<Booking> findById(Long id);

    @EntityGraph(attributePaths = {"user", "asset"})
    Page<Booking> findAll(Pageable pageable);
}
