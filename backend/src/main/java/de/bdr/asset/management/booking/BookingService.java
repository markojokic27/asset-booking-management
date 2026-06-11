package de.bdr.asset.management.booking;

import de.bdr.asset.management.booking.dto.RecurringBookingCreateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.report.dto.GeneralReportResponseDTO;

import java.util.List;

/**
 * Booking Service
 */
public interface BookingService {

    /** CREATE SINGLE*/
    BookingResponseDTO createBooking(BookingCreateDTO bookingRequest);

    /** CREATE RECURRING*/
    List<BookingResponseDTO> createRecurringBookings(RecurringBookingCreateDTO bookingRequest);

    /** READ */
    BookingResponseDTO getBookingById(Long id);
    Page<BookingResponseDTO> getAllBookings(BookingFilter filter, Pageable pageable);

    /** UPDATE */
    BookingResponseDTO updateBooking(Long id, BookingUpdateDTO bookingRequest);

    /** APPROVE */
    public BookingResponseDTO approveBooking(Long bookingId);

    /** REJECT */
    public BookingResponseDTO rejectBooking(Long bookingId);

    int bookingStatusToCompleted();
    GeneralReportResponseDTO getGeneralReport();
    GeneralReportResponseDTO getUserReport(Long userId);
    GeneralReportResponseDTO getAssetReport(Long assetId);
}
