package de.bdr.asset.management.booking;

import java.util.List;

/**
 * Booking Service
 */
public interface BookingService {

    /** CREATE */
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequest);

    /** READ */
    BookingResponseDTO getBookingById(Long id);
    List<BookingResponseDTO> getAllBookings();

    /** UPDATE */
    BookingResponseDTO updateBooking(Long id, BookingRequestDTO bookingRequest);

    /** DELETE (Soft) */
    void deleteBooking(Long id);
}
