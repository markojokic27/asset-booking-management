package de.bdr.asset.management.booking;

import java.util.List;

import org.springframework.stereotype.Service;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.asset.AssetRepository;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;

/**
 * Implementation of Booking Service
 */
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final BookingMapper mapper;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    public BookingServiceImpl(BookingRepository repository, BookingMapper mapper, UserRepository userRepository, AssetRepository assetRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
    }

    /**
     * Create booking in DB.
     *
     * @param bookingRequest - a BookingDTO record
     * @return an BookingResponseDTO record
     */
    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {
        User user = userRepository.findById(bookingRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bookingRequest.userId()));

        Asset asset = assetRepository.findById(bookingRequest.assetId())
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + bookingRequest.assetId()));
        
        Booking booking = mapper.toEntity(bookingRequest);
        booking.setUser(user);
        booking.setAsset(asset);
        booking = repository.save(booking);

        return mapper.toResponse(booking);
    }

    /**
     * Returns a specific booking.
     *
     * @param id - a Long id
     * @return an BookingResponseDTO record
     */
    @Override
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        return mapper.toResponse(booking);
    }

    /**
     * Returns a list of bookings.
     *
     * @return a list of BookingResponseDTO records
     */
    @Override
    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = repository.findAll();

        return bookings.stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Update and return a specific booking.
     *
     * @param id - a Long id
     * @param bookingRequest - an BookingRequestDTO record
     * @return an BookingResponseDTO record
     */
    @Override
    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO bookingRequest) {
        Booking booking = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        User user = userRepository.findById(bookingRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bookingRequest.userId()));

        Asset asset = assetRepository.findById(bookingRequest.assetId())
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + bookingRequest.assetId()));
        
        booking.setUser(user);
        booking.setAsset(asset);
        booking.setStatus(bookingRequest.status());
        booking.setBookingStartTime(bookingRequest.bookingStartTime());
        booking.setBookingEndTime(bookingRequest.bookingEndTime());
        booking.setNotes(bookingRequest.notes());
        booking = repository.save(booking);

        return mapper.toResponse(booking);
    }

    /**
     * Delete a specific booking.
     *
     * @param id - a Long id
     * @implNote Should be a soft delete by setting it to inactive or such
     */
    @Override
    public void deleteBooking(Long id) {
        // TODO: Add a field for soft delete

        // Booking booking = repository.findById(id)
        //     .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id:" + id));

        // booking.setStatus("DELETED"),

        // repository.save(booking);
    }
}
