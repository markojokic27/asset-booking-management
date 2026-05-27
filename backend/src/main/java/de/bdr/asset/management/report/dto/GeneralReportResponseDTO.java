package de.bdr.asset.management.report.dto;

public record GeneralReportResponseDTO (
    Long totalBookingsCount,

    Long totalCompletedBookingCount,
    Long totalCancelledBookingCount,
    Long totalPendingBookingCount,
    Long totalApprovedBookingCount,
    Long totalRejectedBookingCount
) {}
