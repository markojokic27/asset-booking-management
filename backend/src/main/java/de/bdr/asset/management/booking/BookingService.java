package de.bdr.asset.management.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;

/**
 * Booking Service
 */
public interface BookingService {

    /** CREATE */
    BookingResponseDTO createBooking(BookingCreateDTO bookingRequest);

    /** READ */
    BookingResponseDTO getBookingById(Long id);
    Page<BookingResponseDTO> getAllBookings(BookingFilter filter, Pageable pageable);

    /** UPDATE */
    BookingResponseDTO updateBooking(Long id, BookingUpdateDTO bookingRequest);

    /** DELETE (Soft) */
    void deleteBooking(Long id);
}
