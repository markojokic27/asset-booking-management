package de.bdr.asset.management.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Booking Service
 */
public interface BookingService {

    /** CREATE */
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequest);

    /** READ */
    BookingResponseDTO getBookingById(Long id);
    Page<BookingResponseDTO> getAllBookings(Pageable pageable);

    /** UPDATE */
    BookingResponseDTO updateBooking(Long id, BookingRequestDTO bookingRequest);

    /** DELETE (Soft) */
    void deleteBooking(Long id);
}
