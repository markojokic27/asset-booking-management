package de.bdr.asset.management.report.dto;

import java.util.List;

public record GeneralReportResponseDTO (
    Long totalBookingsCount,

    Long totalCompletedBookingCount,
    Long totalCancelledBookingCount,
    Long totalPendingBookingCount,
    Long totalApprovedBookingCount,
    Long totalRejectedBookingCount,

    List<TopUserBookingCountDTO> topUsers,
    List<TopAssetBookingCountDTO> topAssets,

    List<MonthlyBookingStatsDTO> monthlyStats
) {}
