package de.bdr.asset.management.booking;

import java.time.Instant;

import lombok.Data;

@Data
public class BookingFilter {
    private BookingStatusEnum status;
    private Long userId;
    private Long assetId;
    private Long categoryId;
    
    private Instant bookingStart;
    private Instant bookingEnd;
}
