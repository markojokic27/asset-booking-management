package de.bdr.asset.management.booking;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.asset.AssetRepository;
import de.bdr.asset.management.asset.AssetStatusEnum;
import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.core.exception.ActionNotAllowedException;
import de.bdr.asset.management.core.exception.InvalidDateRangeException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.core.security.SecurityService;
import de.bdr.asset.management.report.dto.GeneralReportResponseDTO;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
import de.bdr.asset.management.user.UserStatusEnum;
import lombok.extern.slf4j.Slf4j;

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
    private final SecurityService securityService;
    private final Clock clock;

    public BookingServiceImpl(
        BookingRepository repository,
        BookingMapper mapper,
        UserRepository userRepository,
        AssetRepository assetRepository,
        SecurityService securityService,
        Clock clock
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.securityService = securityService;
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
        this.clock = clock;
    }

    private Instant now() {
        return Instant.now(clock);
    }

    /*
        Helper function for checking if the start is before end.
    */

    public void isStartEndValid(Instant bookingStart, Instant bookingEnd) 
        throws InvalidDateRangeException
    {   
        if (!bookingStart.isBefore(bookingEnd)) {
            throw new InvalidDateRangeException("Booking end time must be after the start time");
        }
    }

    /**
     * Create booking in DB.
     *
     * @param bookingRequest - a BookingDTO record
     * @return an BookingResponseDTO record
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingResponseDTO createBooking(BookingCreateDTO bookingRequest) {

        log.info("Attempting to create a new booking with user id: {} and asset id: {}", bookingRequest.userId(), bookingRequest.assetId());

        // TODO: Maybe an additional check to see if the booking period is some minimum, possibly taken from the controller.
        isStartEndValid(bookingRequest.bookingStart(), bookingRequest.bookingEnd());

        Long loggedInUserId = securityService.getCurrentUserId();

        if (!securityService.isAdmin() && !bookingRequest.userId().equals(loggedInUserId)) {
            throw new AccessDeniedException("Cannot create booking for another user");
        }

        List<UserStatusEnum> validStatuses = List.of(
            UserStatusEnum.ACTIVE,
            UserStatusEnum.STUDENT
        );
        
        User user = userRepository.findByIdAndStatusIn(bookingRequest.userId(), validStatuses)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bookingRequest.userId()));

        Asset asset = assetRepository.findByIdAndStatus(bookingRequest.assetId(), AssetStatusEnum.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + bookingRequest.assetId() + " and status ACTIVE"));
        
        // TODO: Check if this should hold validation logic like if the category is active or not.
        AssetCategory category = asset.getCategory();
        log.debug("User and asset found. Mapping entity and saving to database...");
        
        Booking booking = mapper.toEntity(bookingRequest);
        
        booking.setUser(user);
        booking.setAsset(asset);
        booking.setStatus(category.isApproval() ? BookingStatusEnum.PENDING : BookingStatusEnum.APPROVED);

        repository.save(booking);
        
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
    public Page<BookingResponseDTO> getAllBookings(BookingFilter filter, Pageable pageable) {

        log.debug("Fetching bookings from the database with pagination: " +
                        "Page number: {} | Page size: {} | Sort: {}",
                        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()
        );

        Specification<Booking> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getUserId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("user").get("id"), filter.getUserId()));
        }

        if (filter.getAssetId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("asset").get("id"), filter.getAssetId()));
        }

        if (filter.getCategoryId() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("asset").get("category").get("id"), filter.getCategoryId()));
        }

        if (filter.getBookingStart() != null) {
            spec = spec.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("bookingStart"), filter.getBookingStart()));
        }

        if (filter.getBookingEnd() != null) {
            spec = spec.and((root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("bookingEnd"), filter.getBookingEnd()));
        }

        Page<Booking> bookings = repository.findAll(spec, pageable);

        log.info("Successfully fetched {} bookings", bookings.getNumberOfElements());

        return bookings.map(mapper::toResponse);
    }

    /**
     * Update and return a specific booking.
     *
     * @param id - a Long id
     * @param bookingRequest - an BookingUpdateDTO record
     * @return an BookingResponseDTO record
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingResponseDTO updateBooking(Long id, BookingUpdateDTO bookingRequest) {

        log.info("Attempting to update booking with id: {}", id);

        Booking booking = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (booking.getStatus() == BookingStatusEnum.CANCELLED) {
            throw new ActionNotAllowedException("Cannot update a cancelled booking");
        }

        if (booking.getBookingEnd() != null &&
            booking.getBookingEnd().isBefore(now())) {
            throw new ActionNotAllowedException("Cannot update a booking that has already finished");
        }

        mapper.updateBookingFromDTO(bookingRequest, booking);

        isStartEndValid(booking.getBookingStart(), booking.getBookingEnd());

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
    @Transactional(rollbackFor = Exception.class)
    public void deleteBooking(Long id) {

        // TODO: Add a field for soft delete

        // Booking booking = repository.findById(id)
        //     .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id:" + id));

        // booking.setStatus("DELETED"),

        // repository.save(booking);
    }

    @Override
    public GeneralReportResponseDTO getGeneralReport() {
        return repository.getGeneralReport();
    }

    @Override
    public GeneralReportResponseDTO getUserReport(Long userId) {
        return repository.getUserReport(userId);
    }

    @Override
    public GeneralReportResponseDTO getAssetReport(Long assetId) {
        return repository.getAssetReport(assetId);
    }
}
