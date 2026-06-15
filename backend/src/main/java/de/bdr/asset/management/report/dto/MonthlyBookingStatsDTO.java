package de.bdr.asset.management.report.dto;

public record MonthlyBookingStatsDTO(
    Integer year,
    Integer month,

    Long totalBookingsCount,
    Long totalCompletedBookingCount,
    Long totalCancelledBookingCount,
    Long totalPendingBookingCount,
    Long totalApprovedBookingCount,
    Long totalRejectedBookingCount
) {
}
