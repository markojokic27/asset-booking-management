package de.bdr.asset.management.booking;

import java.time.Instant;
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
    public BookingRequestDTO createBooking(BookingRequestDTO bookingRequest) {
        // TODO: Implement a mapper function to handle this
        
        return new BookingRequestDTO(
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
    public BookingRequestDTO getBookingById(Long id) {
        return new BookingRequestDTO(
            1L,
            1L,
            BookingStatusEnum.APPROVED,
            Instant.now(),
            Instant.now(),
            "Dummy Notes"
        );
    }

    /**
     * Returns a list of bookings.
     *
     * @return a list of BookingResponseDTO records
     */
    @Override
    public List<BookingRequestDTO> getAllBookings() {
        List<BookingRequestDTO> dummyList = new ArrayList<>();

        dummyList.add(
            new BookingRequestDTO(
                1L,
                1L,
                BookingStatusEnum.APPROVED,
                Instant.now(),
                Instant.now(),
                "Dummy Notes 1"
            )
        );

        dummyList.add(
            new BookingRequestDTO(
                1L,
                2L,
                BookingStatusEnum.PENDING,
                Instant.now(),
                Instant.now(),
                "Dummy Notes 2"
            )
        );

        return dummyList;
    }

    /**
     * Update and return a specific booking.
     *
     * @param id - a Long id
     * @param bookingRequest - an BookingRequestDTO record
     * @return an BookingResponseDTO record
     */
    @Override
    public BookingRequestDTO updateBooking(Long id, BookingRequestDTO bookingRequest) {
        return new BookingRequestDTO(
            1L,
            1L,
            BookingStatusEnum.APPROVED,
            Instant.now(),
            Instant.now(),
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
