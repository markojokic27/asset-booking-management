package de.bdr.asset.management.report.projections;

public interface TopAssetBookingsProjection {

    Long getAssetId();

    String getAssetName();

    Long getBookingCount();
}
