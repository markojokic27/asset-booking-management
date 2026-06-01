package de.bdr.asset.management.booking;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import de.bdr.asset.management.core.email.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import de.bdr.asset.management.report.dto.TopAssetBookingCountDTO;
import de.bdr.asset.management.report.dto.TopUserBookingCountDTO;
import de.bdr.asset.management.report.projections.GeneralReportProjection;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
import de.bdr.asset.management.user.UserStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of Booking Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final BookingMapper mapper;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final SecurityService securityService;
    private final Clock clock;
    private final EmailService emailService;

    private Instant now() {
        return Instant.now(clock);
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

        Long loggedInUserId = securityService.getCurrentUserId();

        if (!securityService.isAdmin() && !bookingRequest.userId().equals(loggedInUserId)) {
            throw new AccessDeniedException("Cannot create booking for another user");
        }

        List<UserStatusEnum> validUserStatuses = List.of(
            UserStatusEnum.ACTIVE,
            UserStatusEnum.STUDENT
        );
        
        User user = userRepository.findByIdAndStatusIn(bookingRequest.userId(), validUserStatuses)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bookingRequest.userId()));

        Asset asset = assetRepository.findByIdAndStatus(bookingRequest.assetId(), AssetStatusEnum.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + bookingRequest.assetId() + " and status ACTIVE"));
        
        AssetCategory category = asset.getCategory();

        Booking booking = mapper.toEntity(bookingRequest);
        
        booking.setUser(user);
        booking.setAsset(asset);

        booking.setStatus(category.isApproval() ? BookingStatusEnum.PENDING : BookingStatusEnum.APPROVED);

        booking = repository.save(booking);

        if (category.isApproval()) {

            String approvalLink = "http://localhost:5173/approvals/" + booking.getId();

            String managerEmail = user.getManagerEmail();
            String employeeName = user.getName() + " " + user.getSurname();
            String assetName = asset.getName();

            emailService.sendApprovalEmail(
                    managerEmail,
                    assetName,
                    employeeName,
                    approvalLink
            );
        }

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

        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    /**
     * Returns a list of bookings.
     *
     * @param pageable - a Pageable object that determines page, size and sort
     * @return a list of BookingResponseDTO records
     */
    @Override
    public Page<BookingResponseDTO> getAllBookings(BookingFilter filter, Pageable pageable) {

        return repository.findAll(BookingSpecs.withFilter(filter), pageable)
                .map(mapper::toResponse);
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

        Booking booking = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (booking.getStatus() == BookingStatusEnum.CANCELLED) {
            throw new ActionNotAllowedException("Cannot update a cancelled booking");
        }

        if (booking.getBookingEnd() != null && booking.getBookingEnd().isBefore(now())) {
            throw new ActionNotAllowedException("Cannot update a booking that has already finished");
        }

        if (bookingRequest.status() != null) {
            booking.setStatus(bookingRequest.status());
        }

        mapper.updateBookingFromDTO(bookingRequest, booking);

        if (booking.getBookingStart() != null && booking.getBookingEnd() != null) {
            if (!booking.getBookingEnd().isAfter(booking.getBookingStart())) {
                throw new InvalidDateRangeException("End time must be after start time");
            }
        }

        booking = repository.save(booking);

        return mapper.toResponse(booking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingResponseDTO approveBooking(Long bookingId) {

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (!booking.getStatus().equals(BookingStatusEnum.PENDING)) {
            throw new IllegalStateException("Only pending bookings can be approved.");
        }

        Long loggedInUserId = securityService.getCurrentUserId();
        User employee = booking.getUser();

        User loggedInUser = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Logged in user not found"));

        boolean isManager = employee.getManagerEmail().equalsIgnoreCase(loggedInUser.getEmail());

        if (!securityService.isAdmin() && !isManager) {
            throw new AccessDeniedException("You are not authorized to approve this booking.");
        }

        booking.setStatus(BookingStatusEnum.APPROVED);
        repository.save(booking);

        emailService.sendStatusNotificationEmail(
                employee.getEmail(),
                booking.getAsset().getName(),
                booking.getStatus().name()
        );

        return mapper.toResponse(booking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingResponseDTO rejectBooking(Long bookingId) {

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (!booking.getStatus().equals(BookingStatusEnum.PENDING)) {
            throw new IllegalStateException("Only pending bookings can be rejected.");
        }

        Long loggedInUserId = securityService.getCurrentUserId();
        User employee = booking.getUser();

        User loggedInUser = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Logged in user not found"));

        boolean isManager = employee.getManagerEmail().equalsIgnoreCase(loggedInUser.getEmail());

        if (!securityService.isAdmin() && !isManager) {
            throw new AccessDeniedException("You are not authorized to reject this booking.");
        }

        booking.setStatus(BookingStatusEnum.REJECTED);
        repository.save(booking);

        emailService.sendStatusNotificationEmail(
                employee.getEmail(),
                booking.getAsset().getName(),
                booking.getStatus().name()
        );

        return mapper.toResponse(booking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int bookingStatusToCompleted() {

        Instant currentTime = Instant.now();

        return repository.updateCompletedBookings(currentTime);
    }

    @Override
    public GeneralReportResponseDTO getGeneralReport() {

        GeneralReportProjection stats = repository.getGeneralStats();

        List<TopUserBookingCountDTO> topUsers =
                repository.getTopUsers()
                        .stream()
                        .map(p -> new TopUserBookingCountDTO(
                                p.getUserId(),
                                p.getFullName(),
                                p.getBookingCount()
                        ))
                        .toList();

        List<TopAssetBookingCountDTO> topAssets =
                repository.getTopAssets()
                        .stream()
                        .map(p -> new TopAssetBookingCountDTO(
                                p.getAssetId(),
                                p.getAssetName(),
                                p.getBookingCount()
                        ))
                        .toList();

        return new GeneralReportResponseDTO(
            stats.getTotalBookingsCount(),
            stats.getTotalCompletedBookingCount(),
            stats.getTotalCancelledBookingCount(),
            stats.getTotalPendingBookingCount(),
            stats.getTotalApprovedBookingCount(),
            stats.getTotalRejectedBookingCount(),
            topUsers,
            topAssets
        );
    }

    // @Override
    // public GeneralReportResponseDTO getGeneralReport() {
    //     return repository.getGeneralReport();
    // }

    @Override
    public GeneralReportResponseDTO getUserReport(Long userId) {
        return repository.getUserReport(userId);
    }

    @Override
    public GeneralReportResponseDTO getAssetReport(Long assetId) {
        return repository.getAssetReport(assetId);
    }
}
