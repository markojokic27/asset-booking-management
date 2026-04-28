package de.bdr.asset.management.booking;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BookingFilter {
    private BookingStatusEnum status;
    private Long userId;
    private Long assetId;
    
    private LocalDate bookingStart;
    private LocalDate bookingEnd;
}
