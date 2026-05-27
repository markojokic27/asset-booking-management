package de.bdr.asset.management.report.dto;

public record TopAssetBookingCountDTO (
    Long assetId,
    String name,
    Long bookingCount
) {}
