package de.bdr.asset.management.booking;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.bdr.asset.management.asset.AssetService;
import de.bdr.asset.management.report.ReportFilter;
import de.bdr.asset.management.booking.dto.*;
import de.bdr.asset.management.core.email.EmailService;
import de.bdr.asset.management.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.core.exception.ActionNotAllowedException;
import de.bdr.asset.management.core.exception.InvalidDateRangeException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.core.security.SecurityService;
import de.bdr.asset.management.report.dto.GeneralReportResponseDTO;
import de.bdr.asset.management.report.dto.MonthlyBookingStatsDTO;
import de.bdr.asset.management.report.dto.TopAssetBookingCountDTO;
import de.bdr.asset.management.report.dto.TopUserBookingCountDTO;
import de.bdr.asset.management.report.projections.GeneralReportProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * Implementation of Booking Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    @Value("${app.frontend.base-url}") // Spring resolves this at startup
    private String frontendBaseUrl;

    private final BookingRepository repository;
    private final BookingMapper mapper;
    private final UserService userService;
    private final AssetService assetService;
    private final SecurityService securityService;
    private final Clock clock;
    private final EmailService emailService;

    private Instant now() {
        return Instant.now(clock);
    }

    private record BookingValidationContext(User user, Asset asset) {
    }

    /**
     * Create single booking in DB.
     *
     * @param bookingRequest - a BookingDTO record
     * @return a BookingResponseDTO record
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingResponseDTO createBooking(BookingCreateDTO bookingRequest) {

        BookingValidationContext context = validateAndGetContext(bookingRequest.userId(), bookingRequest.assetId());

        User user = context.user();
        Asset asset = context.asset();

        AssetCategory category = asset.getCategory();

        boolean isPrivilegedUser = securityService.isAdmin() || user.getRole().equals(UserRoleEnum.MANAGER);
        boolean requiresApproval = category.isApproval() && !isPrivilegedUser;

        Booking booking = mapper.toEntity(bookingRequest);

        booking.setUser(user);
        booking.setAsset(asset);

        booking.setStatus(requiresApproval ? BookingStatusEnum.PENDING : BookingStatusEnum.APPROVED);

        booking = repository.save(booking);

        if (requiresApproval) {

            String approvalLink = frontendBaseUrl + "/approvals/" + booking.getId();
            log.debug(">>> APPROVAL LINK BEING SENT: {}", approvalLink);
            emailService.sendApprovalEmail(
                    user.getManagerEmail(),
                    asset.getName(),
                    user.getName() + " " + user.getSurname(),
                    approvalLink);
        }

        return mapper.toResponse(booking);
    }

    /**
     * Create recurring bookings in DB.
     *
     * @param bookingRequest - a BookingDTO record
     * @return a list of BookingResponseDTO records
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BookingResponseDTO> createRecurringBookings(RecurringBookingCreateDTO bookingRequest) {

        BookingValidationContext context = validateAndGetContext(bookingRequest.userId(), bookingRequest.assetId());
        User user = context.user();
        Asset asset = context.asset();

        AssetCategory category = asset.getCategory();

        boolean isPrivilegedUser = securityService.isAdmin() || user.getRole().equals(UserRoleEnum.MANAGER);
        boolean requiresApproval = category.isApproval() && !isPrivilegedUser;

        BookingStatusEnum status = requiresApproval ? BookingStatusEnum.PENDING : BookingStatusEnum.APPROVED;

        List<Booking> bookingsToSave = new ArrayList<>();

        for (TimeSlotDTO slot : bookingRequest.timeSlots()) {

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setAsset(asset);
            booking.setNotes(bookingRequest.notes());
            booking.setBookingStart(slot.bookingStart());
            booking.setBookingEnd(slot.bookingEnd());
            booking.setStatus(status);

            bookingsToSave.add(booking);
        }

        List<Booking> savedBookings = repository.saveAll(bookingsToSave);

        if (requiresApproval) {

            String idsParam = savedBookings.stream()
                    .map(booking -> String.valueOf(booking.getId()))
                    .collect(Collectors.joining(","));

            String approvalLink = frontendBaseUrl + "/approvals/bulk?ids=" + idsParam;

            emailService.sendApprovalEmail(
                    user.getManagerEmail(),
                    asset.getName() + " (Multiple Dates)",
                    user.getName() + " " + user.getSurname(),
                    approvalLink);
        }

        return savedBookings.stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Returns a specific booking.
     *
     * @param id - a Long id
     * @return a BookingResponseDTO record
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
     * @param id             - a Long id
     * @param bookingRequest - an BookingUpdateDTO record
     * @return a BookingResponseDTO record
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

        booking.setStatus(BookingStatusEnum.APPROVED);
        repository.save(booking);

        emailService.sendStatusNotificationEmail(
                booking.getUser().getEmail(),
                booking.getAsset().getName(),
                booking.getStatus().name());

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

        booking.setStatus(BookingStatusEnum.REJECTED);
        repository.save(booking);

        emailService.sendStatusNotificationEmail(
                booking.getUser().getEmail(),
                booking.getAsset().getName(),
                booking.getStatus().name());

        return mapper.toResponse(booking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int bookingStatusToCompleted() {

        Instant currentTime = Instant.now();

        return repository.updateCompletedBookings(currentTime);
    }

    @Override
    public GeneralReportResponseDTO getGeneralReport(ReportFilter filter) {

        Instant fromDate = filter.getFromDate();
        Instant toDate = filter.getToDate();

        if (fromDate == null && toDate == null) {
            fromDate = LocalDateTime.now()
                    .minusYears(1)
                    .atZone(ZoneId.systemDefault())
                    .toInstant();

            toDate = LocalDateTime.now()
                    .atZone(ZoneId.systemDefault())
                    .toInstant();
        }

        GeneralReportProjection stats = repository.getGeneralStats(
                fromDate,
                toDate,
                filter.getUserId(),
                filter.getAssetId());

        List<TopUserBookingCountDTO> topUsers = repository.getTopUsers(
                fromDate,
                toDate,
                filter.getAssetId())
                .stream()
                .map(p -> new TopUserBookingCountDTO(
                        p.getUserId(),
                        p.getFullName(),
                        p.getBookingCount()))
                .toList();

        List<TopAssetBookingCountDTO> topAssets = repository.getTopAssets(
                fromDate,
                toDate,
                filter.getUserId())
                .stream()
                .map(p -> new TopAssetBookingCountDTO(
                        p.getAssetId(),
                        p.getAssetName(),
                        p.getBookingCount()))
                .toList();

        List<MonthlyBookingStatsDTO> monthlyStats = repository.getMonthlyStats(
                fromDate,
                toDate,
                filter.getUserId(),
                filter.getAssetId())
                .stream()
                .map(p -> new MonthlyBookingStatsDTO(
                        p.getYear(),
                        p.getMonth(),
                        p.getTotalBookingsCount(),
                        p.getTotalCompletedBookingCount(),
                        p.getTotalCancelledBookingCount(),
                        p.getTotalPendingBookingCount(),
                        p.getTotalApprovedBookingCount(),
                        p.getTotalRejectedBookingCount()))
                .toList();

        return new GeneralReportResponseDTO(
                stats.getTotalBookingsCount(),
                stats.getTotalCompletedBookingCount(),
                stats.getTotalCancelledBookingCount(),
                stats.getTotalPendingBookingCount(),
                stats.getTotalApprovedBookingCount(),
                stats.getTotalRejectedBookingCount(),
                topUsers,
                topAssets,
                monthlyStats);
    }

    private BookingValidationContext validateAndGetContext(Long requestedUserId, Long assetId) {

        Long targetUserId = requestedUserId != null
                ? requestedUserId
                : securityService.getCurrentUserId();

        User user = userService.getActiveOrStudentUserById(targetUserId);
        Asset asset = assetService.getActiveAssetById(assetId);

        return new BookingValidationContext(user, asset);
    }
}
