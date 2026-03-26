package de.bdr.asset.management.booking;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of Booking Service
 */
@Service
public class BookingServiceImpl implements BookingService {

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

        // TODO Implement...

        return null;
    }

    /**
     * Returns a specific booking.
     *
     * @param id - a Long id
     * @return an BookingResponseDTO record
     */
    @Override
    public BookingDTO getBookingById(Long id) {

        // TODO Implement...

        return null;
    }

    /**
     * Returns a list of bookings.
     *
     * @return a list of BookingResponseDTO records
     */
    @Override
    public List<BookingDTO> getAllBookings() {

        // TODO Implement...

        return null;
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

        // TODO Implement...

        return null;
    }

    /**
     * Delete a specific booking.
     *
     * @param id - a Long id
     * @implNote Should be a soft delete by setting it to inactive or such
     */
    @Override
    public void deleteBooking(Long id) {

        // TODO Implement...
    }
}
