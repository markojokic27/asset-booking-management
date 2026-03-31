package de.bdr.asset.management.booking;

import java.util.List;

/**
 * Booking Service
 */
public interface BookingService {

    /** CREATE */
    BookingRequestDTO createBooking(BookingRequestDTO bookingRequest);

    /** READ */
    BookingRequestDTO getBookingById(Long id);
    List<BookingRequestDTO> getAllBookings();

    /** UPDATE */
    BookingRequestDTO updateBooking(Long id, BookingRequestDTO bookingRequest);

    /** DELETE (Soft) */
    void deleteBooking(Long id);
}
