package de.bdr.asset.management.booking;

import de.bdr.asset.management.core.exception.ActionNotAllowedException;
import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.InvalidDateRangeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.asset.AssetRepository;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Implementation of Booking Service
 */
@Slf4j
@Service
@Transactional(readOnly = true)
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
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {

        log.info("Attempting to create a new booking with user id: {} and asset id: {}", bookingRequest.userId(), bookingRequest.assetId());

        if (!bookingRequest.bookingEnd().isAfter(bookingRequest.bookingStart())) {
            throw new InvalidDateRangeException("Booking end time must be after the start time");
        }

        if (bookingRequest.bookingStart().isBefore(Instant.now())) {
            throw new InvalidDateRangeException("Booking start time cannot be in the past");
        }

        User user = userRepository.findById(bookingRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bookingRequest.userId()));

        Asset asset = assetRepository.findById(bookingRequest.assetId())
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + bookingRequest.assetId()));

        int overlapCount = repository.countOverlappingBookings(
                bookingRequest.assetId(),
                bookingRequest.bookingStart(),
                bookingRequest.bookingEnd()
        );

        if (overlapCount > 0) {
            throw new DuplicateResourceException("The selected time slot is already booked for asset ID: " + bookingRequest.assetId());
        }

        log.debug("User and asset found. Mapping entity and saving to database...");
        
        Booking booking = mapper.toEntity(bookingRequest);
        booking.setUser(user);
        booking.setAsset(asset);
        booking = repository.save(booking);

        log.info("Successfully created new booking with id: {} for user id: {} with asset id: {}", booking.getId(), user.getId(), asset.getId());

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

        log.info("Booking found with id: {}", id);

        return mapper.toResponse(booking);
    }

    /**
     * Returns a list of bookings.
     *
     * @param pageable - a Pageable object that determines page, size and sort
     * @return a list of BookingResponseDTO records
     */
    @Override
    public Page<BookingResponseDTO> getAllBookings(Pageable pageable) {

        log.debug("Fetching bookings from the database with pagination: " +
                        "Page number: {} | Page size: {} | Sort: {}",
                        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()
        );

        Page<Booking> bookings = repository.findAll(pageable);

        log.info("Successfully fetched {} bookings", bookings.getNumberOfElements());

        return bookings.map(mapper::toResponse);
    }

    /**
     * Update and return a specific booking.
     *
     * @param id - a Long id
     * @param bookingRequest - an BookingRequestDTO record
     * @return an BookingResponseDTO record
     */
    @Override
    @Transactional
    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO bookingRequest) {

        log.info("Attempting to update booking with id: {}", id);

        Booking booking = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (booking.getStatus() == BookingStatusEnum.CANCELLED) {
            throw new ActionNotAllowedException("Cannot update a cancelled booking");
        }

        if (booking.getBookingEnd().isBefore(Instant.now())) {
            throw new ActionNotAllowedException("Cannot update a booking that has already finished");
        }

        if (!bookingRequest.bookingEnd().isAfter(bookingRequest.bookingStart())) {
            throw new InvalidDateRangeException("Booking end time must be after the start time");
        }

        if (bookingRequest.bookingStart().isBefore(Instant.now())) {
            throw new InvalidDateRangeException("New booking start time cannot be in the past");
        }

        int overlapCount = repository.countOverlappingBookingsForUpdate(
                bookingRequest.assetId(),
                bookingRequest.bookingStart(),
                bookingRequest.bookingEnd(),
                id
        );

        if (overlapCount > 0) {
            throw new DuplicateResourceException("The selected time slot is already booked for asset ID: " + bookingRequest.assetId());
        }

        User user = userRepository.findById(bookingRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bookingRequest.userId()));

        Asset asset = assetRepository.findById(bookingRequest.assetId())
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + bookingRequest.assetId()));
        
        booking.setUser(user);
        booking.setAsset(asset);
        booking.setStatus(bookingRequest.status());
        booking.setBookingStart(bookingRequest.bookingStart());
        booking.setBookingEnd(bookingRequest.bookingEnd());
        booking.setNotes(bookingRequest.notes());
        booking = repository.save(booking);

        log.info("Successfully updated booking with id: {}", id);

        return mapper.toResponse(booking);
    }

    /**
     * Delete a specific booking.
     *
     * @param id - a Long id
     * @implNote Should be a soft delete by setting it to inactive or such
     */
    @Override
    @Transactional
    public void deleteBooking(Long id) {

        // TODO: Add a field for soft delete

        // Booking booking = repository.findById(id)
        //     .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id:" + id));

        // booking.setStatus("DELETED"),

        // repository.save(booking);
    }
}
