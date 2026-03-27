package de.bdr.asset.management.booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Implementation of Booking Service
 */
@Service
public class BookingServiceImpl implements BookingService {
    // TODO: Update the functions to not use dummy data
    private final BookingRepository repository;

    public BookingServiceImpl(BookingRepository repository) {
        this.repository = repository;
    }

    /**
     * Create booking in DB.
     *
     * @param bookingRequest - a BookingDTO record
     * @return an BookingResponseDTO record
     */
    @Override
    public BookingDTO createBooking(BookingDTO bookingRequest) {
        // TODO: Implement a mapper function to handle this
        
        return new BookingDTO(
            1L,
            bookingRequest.userId(),
            bookingRequest.assetId(),
            bookingRequest.status(),
            bookingRequest.bookingStartTime(),
            bookingRequest.bookingEndTime(),
            bookingRequest.notes()
        );
    }

    /**
     * Returns a specific booking.
     *
     * @param id - a Long id
     * @return an BookingResponseDTO record
     */
    @Override
    public BookingDTO getBookingById(Long id) {
        return new BookingDTO(
            1L,
            1L,
            1L,
            BookingStatusEnum.APPROVED,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Dummy Notes"
        );
    }

    /**
     * Returns a list of bookings.
     *
     * @return a list of BookingResponseDTO records
     */
    @Override
    public List<BookingDTO> getAllBookings() {
        List<BookingDTO> dummyList = new ArrayList<>();

        dummyList.add(
            new BookingDTO(
                1L,
                1L,
                1L,
                BookingStatusEnum.APPROVED,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Dummy Notes 1"
            )
        );

        dummyList.add(
            new BookingDTO(
                2L,
                1L,
                2L,
                BookingStatusEnum.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Dummy Notes 2"
            )
        );

        return dummyList;
    }

    /**
     * Update and return a specific booking.
     *
     * @param id - a Long id
     * @param bookingRequest - an BookingDTO record
     * @return an BookingResponseDTO record
     */
    @Override
    public BookingDTO updateBooking(Long id, BookingDTO bookingRequest) {
        return new BookingDTO(
            1L,
            1L,
            1L,
            BookingStatusEnum.APPROVED,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Dummy Notes"
        );
    }

    /**
     * Delete a specific booking.
     *
     * @param id - a Long id
     * @implNote Should be a soft delete by setting it to inactive or such
     */
    @Override
    public void deleteBooking(Long id) {

    }
}
