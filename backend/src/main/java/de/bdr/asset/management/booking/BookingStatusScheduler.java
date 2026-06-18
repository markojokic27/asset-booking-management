package de.bdr.asset.management.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingStatusScheduler {

    private final BookingService bookingService;

    public BookingStatusScheduler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(cron = "${booking.scheduler.cron}", zone = "${booking.scheduler.zone}")
    public void closingBooking() {

        log.info("Scheduler activated. Starting to check finished bookings.");

        int numOfFinishedBookings = bookingService.bookingStatusToCompleted();

        log.info("Scheduler finished. Completed {} bookings.", numOfFinishedBookings);
    }
}
