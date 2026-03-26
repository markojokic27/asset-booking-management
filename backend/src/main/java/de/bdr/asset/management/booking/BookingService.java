package de.bdr.asset.management.booking;

import java.util.List;

/**
 * Booking Service
 */
public interface BookingService {

    /** CREATE */
    BookingDTO createBooking(BookingDTO bookingRequest);

    /** READ */
    BookingDTO getBookingById(Long id);
    List<BookingDTO> getAllBookings();

    /** UPDATE */
    BookingDTO updateBooking(Long id, BookingDTO bookingRequest);

    /** DELETE (Soft) */
    void deleteBooking(Long id);
}
