package de.bdr.asset.management.report.dto;

public record GeneralReportResponseDTO (
    Long totalBookingsCount,

    Long totalActiveBookingCount,
    Long totalCompletedBookingCount,
    Long totalCanceledBookingCount,
    Long totalPendingBookingCount,  
    Long totalApprovedBookingCount,
    Long totalRejectedBookingCount
) {}
