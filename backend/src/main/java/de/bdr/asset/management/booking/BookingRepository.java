package de.bdr.asset.management.booking;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.bdr.asset.management.report.dto.GeneralReportResponseDTO;
import de.bdr.asset.management.report.projections.GeneralReportProjection;
import de.bdr.asset.management.report.projections.TopAssetBookingsProjection;
import de.bdr.asset.management.report.projections.TopUserBookingsProjection;

/**
 * JPA Booking Repository
 */
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

        @EntityGraph(attributePaths = { "user", "asset" })
        Optional<Booking> findById(Long id);

        @EntityGraph(attributePaths = { "user", "asset" })
        Page<Booking> findAll(Specification<Booking> spec, Pageable pageable);

        @Modifying(clearAutomatically = true)
        @Query(value = "UPDATE asset_booking_mgm.booking SET status = 'CANCELLED' " +
                        "WHERE user_id = :userId " +
                        "AND status IN :targetStatuses", nativeQuery = true)
        void cancelNotFinishedBookingsForUser(
                        @Param("userId") Long userId,
                        @Param("targetStatuses") List<String> targetStatuses);

        @Query(value = """
                SELECT
                COUNT(*) AS totalBookingsCount,

                COUNT(*) FILTER (WHERE status = 'COMPLETED') AS totalCompletedBookingCount,
                COUNT(*) FILTER (WHERE status = 'CANCELLED') AS totalCancelledBookingCount,
                COUNT(*) FILTER (WHERE status = 'PENDING') AS totalPendingBookingCount,
                COUNT(*) FILTER (WHERE status = 'APPROVED') AS totalApprovedBookingCount,
                COUNT(*) FILTER (WHERE status = 'REJECTED') AS totalRejectedBookingCount

                FROM asset_booking_mgm.booking
                """, nativeQuery = true)
        GeneralReportProjection getGeneralStats();

        @Query(value = """
                SELECT
                u.id AS userId,
                CONCAT(u.name, ' ', u.surname) AS fullName,
                COUNT(b.id) AS bookingCount
                FROM asset_booking_mgm.booking b
                JOIN asset_booking_mgm.asset_user u ON u.id = b.user_id
                GROUP BY u.id, u.name, u.surname
                ORDER BY bookingCount DESC
                LIMIT 5
                """, nativeQuery = true)
        List<TopUserBookingsProjection> getTopUsers();

        @Query(value = """
                SELECT
                a.id AS assetId,
                a.name AS assetName,
                COUNT(b.id) AS bookingCount
                FROM asset_booking_mgm.booking b
                JOIN asset_booking_mgm.asset a ON a.id = b.asset_id
                GROUP BY a.id, a.name
                ORDER BY bookingCount DESC
                LIMIT 5
                """, nativeQuery = true)
        List<TopAssetBookingsProjection> getTopAssets();

        @Query(value = """
                        SELECT
                            COUNT(*) AS totalBookingsCount,

                            SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) AS totalCompletedBookingCount,
                            SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) AS totalCancelledBookingCount,
                            SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) AS totalPendingBookingCount,
                            SUM(CASE WHEN status = 'APPROVED' THEN 1 ELSE 0 END) AS totalApprovedBookingCount,
                            SUM(CASE WHEN status = 'REJECTED' THEN 1 ELSE 0 END) AS totalRejectedBookingCount

                            FROM asset_booking_mgm.booking
                            WHERE user_id = :userId
                            """, nativeQuery = true)
        GeneralReportResponseDTO getUserReport(@Param("userId") Long userId);

        @Query(value = """
                            SELECT
                                COUNT(*) AS totalBookingsCount,

                                SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) AS totalCompletedBookingCount,
                                SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) AS totalCancelledBookingCount,
                                SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) AS totalPendingBookingCount,
                                SUM(CASE WHEN status = 'APPROVED' THEN 1 ELSE 0 END) AS totalApprovedBookingCount,
                                SUM(CASE WHEN status = 'REJECTED' THEN 1 ELSE 0 END) AS totalRejectedBookingCount

                            FROM asset_booking_mgm.booking
                            WHERE asset_id = :assetId
                        """, nativeQuery = true)
        GeneralReportResponseDTO getAssetReport(@Param("assetId") Long assetId);

        @Modifying(clearAutomatically = true)
        @Query(value = "UPDATE asset_booking_mgm.booking SET status = 'COMPLETED'" +
                       "WHERE status = 'APPROVED'" +
                       "AND booking_end <= :currentTime", nativeQuery = true)
        int updateCompletedBookings(
                @Param("currentTime")Instant currentTime
        );
}
