package de.bdr.asset.management.report.projections;

public interface TopUserBookingsProjection {

    Long getUserId();

    String getFullName();

    Long getBookingCount();
}
