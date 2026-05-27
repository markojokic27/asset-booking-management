package de.bdr.asset.management.report.dto;

public record TopUserBookingCountDTO (
    Long userId,
    String fullName,
    Long bookingCount
) {}
