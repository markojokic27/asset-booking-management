package de.bdr.asset.management.report.projections;

public interface GeneralReportProjection {

    Long getTotalBookingsCount();

    Long getTotalCompletedBookingCount();

    Long getTotalCancelledBookingCount();

    Long getTotalPendingBookingCount();

    Long getTotalApprovedBookingCount();

    Long getTotalRejectedBookingCount();
}