package de.bdr.asset.management.report.projections;

public interface MonthlyBookingStatsProjection {

    Integer getYear();
    Integer getMonth();

    Long getTotalBookingsCount();

    Long getTotalCompletedBookingCount();
    Long getTotalCancelledBookingCount();
    Long getTotalPendingBookingCount();
    Long getTotalApprovedBookingCount();
    Long getTotalRejectedBookingCount();
}