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

import de.bdr.asset.management.report.projections.GeneralReportProjection;
import de.bdr.asset.management.report.projections.MonthlyBookingStatsProjection;
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

                COUNT(*) FILTER (WHERE b.status = 'COMPLETED') AS totalCompletedBookingCount,
                COUNT(*) FILTER (WHERE b.status = 'CANCELLED') AS totalCancelledBookingCount,
                COUNT(*) FILTER (WHERE b.status = 'PENDING') AS totalPendingBookingCount,
                COUNT(*) FILTER (WHERE b.status = 'APPROVED') AS totalApprovedBookingCount,
                COUNT(*) FILTER (WHERE b.status = 'REJECTED') AS totalRejectedBookingCount

        FROM asset_booking_mgm.booking b

        WHERE
                (CAST(:userId as bigint) IS NULL OR b.user_id = :userId)
                AND (CAST(:assetId as bigint) IS NULL OR b.asset_id = :assetId)
                AND (CAST(:fromDate AS timestamp) IS NULL OR b.booking_end >= :fromDate)
                AND (CAST(:toDate AS timestamp) IS NULL OR b.booking_end <= :toDate)
        """,
        nativeQuery = true)
        GeneralReportProjection getGeneralStats(
                @Param("fromDate") Instant fromDate,
                @Param("toDate") Instant toDate,
                @Param("userId") Long userId,
                @Param("assetId") Long assetId
        );

        @Query(value = """
        SELECT
                u.id AS userId,
                CONCAT(u.name, ' ', u.surname) AS fullName,
                COUNT(b.id) AS bookingCount

        FROM asset_booking_mgm.booking b
        JOIN asset_booking_mgm.asset_user u ON u.id = b.user_id

        WHERE
                (:assetId is NULL OR b.asset_id = :assetId)
                AND (CAST(:fromDate AS timestamp) IS NULL OR b.booking_end >= :fromDate)
                AND (CAST(:toDate AS timestamp) IS NULL OR b.booking_end <= :toDate)

        GROUP BY u.id, u.name, u.surname
        ORDER BY bookingCount DESC
        LIMIT 5
        """,
        nativeQuery = true)
        List<TopUserBookingsProjection> getTopUsers(
                @Param("fromDate") Instant fromDate,
                @Param("toDate") Instant toDate,
                @Param("assetId") Long assetId
        );

        @Query(value = """
        SELECT
                a.id AS assetId,
                a.name AS assetName,
                COUNT(b.id) AS bookingCount

        FROM asset_booking_mgm.booking b
        JOIN asset_booking_mgm.asset a ON a.id = b.asset_id

        WHERE
                (CAST(:userId as bigint) is NULL OR b.user_id = :userId)
                AND (CAST(:fromDate AS timestamp) IS NULL OR b.booking_end >= :fromDate)
                AND (CAST(:toDate AS timestamp) IS NULL OR b.booking_end <= :toDate)

        GROUP BY a.id, a.name
        ORDER BY bookingCount DESC
        LIMIT 5
        """,
        nativeQuery = true)
        List<TopAssetBookingsProjection> getTopAssets(
                @Param("fromDate") Instant fromDate,
                @Param("toDate") Instant toDate,
                @Param("userId") Long userId
        );

        @Query(value = """
        SELECT
                EXTRACT(YEAR FROM b.booking_end)::int AS year,
                EXTRACT(MONTH FROM b.booking_end)::int AS month,

                COUNT(*) AS totalBookingsCount,

                COUNT(*) FILTER (WHERE b.status = 'COMPLETED') AS totalCompletedBookingCount,
                COUNT(*) FILTER (WHERE b.status = 'CANCELLED') AS totalCancelledBookingCount,
                COUNT(*) FILTER (WHERE b.status = 'PENDING') AS totalPendingBookingCount,
                COUNT(*) FILTER (WHERE b.status = 'APPROVED') AS totalApprovedBookingCount,
                COUNT(*) FILTER (WHERE b.status = 'REJECTED') AS totalRejectedBookingCount

        FROM asset_booking_mgm.booking b

        WHERE
                (CAST(:userId as bigint) is NULL OR b.user_id = :userId)
                AND (CAST(:assetId as bigint) is NULL OR b.asset_id = :assetId)
                AND (CAST(:fromDate AS timestamp) IS NULL OR b.booking_end >= :fromDate)
                AND (CAST(:toDate AS timestamp) IS NULL OR b.booking_end <= :toDate)

        GROUP BY
                EXTRACT(YEAR FROM b.booking_end),
                EXTRACT(MONTH FROM b.booking_end)

        ORDER BY year, month
        """,
        nativeQuery = true)
        List<MonthlyBookingStatsProjection> getMonthlyStats(
                @Param("fromDate") Instant fromDate,
                @Param("toDate") Instant toDate,
                @Param("userId") Long userId,
                @Param("assetId") Long assetId
        );

        @Modifying(clearAutomatically = true)
        @Query(value = "UPDATE asset_booking_mgm.booking SET status = 'COMPLETED'" +
                       "WHERE status = 'APPROVED'" +
                       "AND booking_end <= :currentTime", nativeQuery = true)
        int updateCompletedBookings(
                @Param("currentTime")Instant currentTime
        );
}
