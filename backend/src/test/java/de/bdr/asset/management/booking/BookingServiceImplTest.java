package de.bdr.asset.management.booking;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.bdr.asset.management.asset.AssetService;
import de.bdr.asset.management.core.email.EmailService;
import de.bdr.asset.management.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import de.bdr.asset.management.asset.Asset;
import static de.bdr.asset.management.booking.TestConstants.ASSET_ID;
import static de.bdr.asset.management.booking.TestConstants.BOOKING_ID;
import static de.bdr.asset.management.booking.TestConstants.USER_ID;
import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.booking.dto.RecurringBookingCreateDTO;
import de.bdr.asset.management.core.exception.ActionNotAllowedException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.core.security.SecurityService;
import de.bdr.asset.management.report.ReportFilter;
import de.bdr.asset.management.report.dto.GeneralReportResponseDTO;
import de.bdr.asset.management.report.projections.GeneralReportProjection;
import de.bdr.asset.management.report.projections.MonthlyBookingStatsProjection;
import de.bdr.asset.management.report.projections.TopAssetBookingsProjection;
import de.bdr.asset.management.report.projections.TopUserBookingsProjection;
import de.bdr.asset.management.user.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository repository;

    @Mock
    private BookingMapper mapper;

    @Mock
    private SecurityService securityService;

    @Mock
    private UserService userService;

    @Mock
    private AssetService assetService;

    @Mock
    private EmailService emailService;

    private BookingServiceImpl service;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        Clock fixedClock = Clock.fixed(
                TestConstants.BASE_NOW,
                ZoneOffset.UTC
        );

        service = new BookingServiceImpl(
                repository,
                mapper,
                userService,
                assetService,
                securityService,
                fixedClock,
                emailService
        );
    }

    private void mockAdminUser() {
        when(securityService.isAdmin()).thenReturn(true);
        when(securityService.getCurrentUserId()).thenReturn(USER_ID);
    }

    // Tests createBooking(): user and asset exist, booking saved
    @Test
    void shouldCreateBooking() {

        User user = BookingServiceImplTestData.user();
        Asset asset = BookingServiceImplTestData.asset();
        Booking booking = BookingServiceImplTestData.booking(user, asset);

        BookingCreateDTO request = BookingServiceImplTestData.createRequest();
        BookingResponseDTO response = BookingServiceImplTestData.response();

        when(userService.getActiveOrStudentUserById(USER_ID)).thenReturn(user);
        when(assetService.getActiveAssetById(ASSET_ID)).thenReturn(asset);
        when(mapper.toEntity(request)).thenReturn(booking);
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(response);

        BookingResponseDTO result = service.createBooking(request);

        assertEquals(response, result);
    }

    // Tests createBooking(): throws if user not found
    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        BookingCreateDTO request = BookingServiceImplTestData.createRequest();

        when(userService.getActiveOrStudentUserById(USER_ID)).thenThrow(new ResourceNotFoundException("User not found with id: " + USER_ID));
        assertThrows(ResourceNotFoundException.class,
                () -> service.createBooking(request));

        verify(repository, never()).save(any());
    }

    // Tests createBooking(): throws if asset not found
    @Test
    void shouldThrowExceptionWhenAssetNotFound() {
        
        User user = BookingServiceImplTestData.user();

        BookingCreateDTO request = BookingServiceImplTestData.createRequest();

        when(userService.getActiveOrStudentUserById(USER_ID)).thenReturn(user);
        when(assetService.getActiveAssetById(ASSET_ID)).thenThrow(new ResourceNotFoundException("Asset not found with id: " + ASSET_ID));

        assertThrows(ResourceNotFoundException.class,
                () -> service.createBooking(request));

        verify(repository, never()).save(any());
    }

    // Tests getBookingById(): booking found
    @Test
    void shouldGetBookingById() {

        Booking booking = BookingServiceImplTestData.booking(
                BookingServiceImplTestData.user(),
                BookingServiceImplTestData.asset()
        );

        BookingResponseDTO response = BookingServiceImplTestData.response();

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        when(mapper.toResponse(booking)).thenReturn(response);

        BookingResponseDTO result = service.getBookingById(BOOKING_ID);

        assertEquals(response, result);
    }

    // Tests getBookingById(): throws if not found
    @Test
    void shouldThrowExceptionWhenBookingNotFound() {

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getBookingById(BOOKING_ID));
    }

    // Tests getAllBookings(): fetch all bookings
    @Test
    void shouldReturnAllBookings() {

        User user = BookingServiceImplTestData.user();
        Asset asset = BookingServiceImplTestData.asset();
        Booking booking = BookingServiceImplTestData.booking(user, asset);

        BookingResponseDTO response = BookingServiceImplTestData.response();

        Pageable pageable = PageRequest.of(0, 10);
        BookingFilter filter = new BookingFilter();
        Page<Booking> bookingPage = new PageImpl<>(List.of(booking));

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookingPage);
        when(mapper.toResponse(booking)).thenReturn(response);

        Page<BookingResponseDTO> result = service.getAllBookings(filter, pageable);

        assertEquals(1, result.getTotalElements());

        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    // Tests getAllBookings(): fetch all bookings with a fully populated filter to cover all 'if' conditions
    @Test
    void shouldReturnAllBookings_WithFullyPopulatedFilter() {

        User user = BookingServiceImplTestData.user();
        Asset asset = BookingServiceImplTestData.asset();
        Booking booking = BookingServiceImplTestData.booking(user, asset);

        BookingResponseDTO response = BookingServiceImplTestData.response();

        Pageable pageable = PageRequest.of(0, 10);
        BookingFilter filter = new BookingFilter();
        filter.setStatus(BookingStatusEnum.PENDING);
        filter.setUserId(USER_ID);
        filter.setAssetId(ASSET_ID);
        filter.setCategoryId(1L);
        filter.setBookingStart(TestConstants.BASE_NOW);
        filter.setBookingEnd(TestConstants.BASE_NOW.plusSeconds(3600));
        Page<Booking> bookingPage = new PageImpl<>(List.of(booking));

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookingPage);
        when(mapper.toResponse(booking)).thenReturn(response);

        Page<BookingResponseDTO> result = service.getAllBookings(filter, pageable);

        assertEquals(1, result.getTotalElements());

        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    // Tests updateBooking(): booking exists, user and asset exist, update saved
    @Test
    void shouldUpdateBooking() {

        User user = BookingServiceImplTestData.user();
        Asset asset = BookingServiceImplTestData.asset();
        Booking booking = BookingServiceImplTestData.booking(user, asset);

        BookingUpdateDTO request = BookingServiceImplTestData.updateRequest();
        BookingResponseDTO response = BookingServiceImplTestData.response();

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(response);

        BookingResponseDTO result = service.updateBooking(BOOKING_ID, request);

        assertEquals(BookingStatusEnum.PENDING, result.status());
        verify(repository).save(booking);
    }

    // Tests updateBooking(): throws if booking not found
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingBooking() {

        BookingUpdateDTO request = BookingServiceImplTestData.updateRequest();

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateBooking(BOOKING_ID, request));
    }

    // Tests updateBooking(): throws exception if booking status is CANCELLED
    @Test
    void shouldThrowExceptionWhenUpdatingCancelledBooking() {

        Booking booking = BookingServiceImplTestData.booking(
                BookingServiceImplTestData.user(),
                BookingServiceImplTestData.asset()
        );
        booking.setStatus(BookingStatusEnum.CANCELLED);

        BookingUpdateDTO request = BookingServiceImplTestData.updateRequest();

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));

        assertThrows(ActionNotAllowedException.class,
                () -> service.updateBooking(BOOKING_ID, request));

        verify(repository).findById(BOOKING_ID);
        verify(repository, never()).save(any());
    }

    // Tests updateBooking(): throws exception if booking has already finished
    @Test
    void shouldThrowExceptionWhenUpdatingFinishedBooking() {

        Booking booking = BookingServiceImplTestData.booking(
                BookingServiceImplTestData.user(),
                BookingServiceImplTestData.asset()
        );
        booking.setStatus(BookingStatusEnum.PENDING);
        booking.setBookingEnd(TestConstants.BASE_NOW.minusSeconds(3600));

        BookingUpdateDTO request = BookingServiceImplTestData.updateRequest();

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));

        assertThrows(ActionNotAllowedException.class,
                () -> service.updateBooking(BOOKING_ID, request));

        verify(repository).findById(BOOKING_ID);
        verify(repository, never()).save(any());
    }

    // ──────────────────────────────────────────────
    // createRecurringBookings
    // ──────────────────────────────────────────────

    @Test
    void shouldCreateRecurringBookings() {

        User user = BookingServiceImplTestData.user();
        Asset asset = BookingServiceImplTestData.asset();
        RecurringBookingCreateDTO request = BookingServiceImplTestData.recurringCreateRequest();

        Booking booking1 = BookingServiceImplTestData.booking(user, asset);
        Booking booking2 = BookingServiceImplTestData.booking(user, asset);
        booking2.setId(TestConstants.BOOKING_ID_2);
        booking2.setBookingStart(TestConstants.START_2);
        booking2.setBookingEnd(TestConstants.END_2);

        BookingResponseDTO response1 = BookingServiceImplTestData.response();
        BookingResponseDTO response2 = BookingServiceImplTestData.approvedResponse();

        when(userService.getActiveOrStudentUserById(USER_ID)).thenReturn(user);
        when(assetService.getActiveAssetById(ASSET_ID)).thenReturn(asset);
        when(securityService.isAdmin()).thenReturn(true);

        List<Booking> saved = List.of(booking1, booking2);
        when(repository.saveAll(any())).thenReturn(saved);
        when(mapper.toResponse(booking1)).thenReturn(response1);
        when(mapper.toResponse(booking2)).thenReturn(response2);

        List<BookingResponseDTO> result = service.createRecurringBookings(request);

        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));

        verify(repository).saveAll(any());
        verify(emailService, never()).sendApprovalEmail(any(), any(), any(), any());
    }

    @Test
    void shouldCreateRecurringBookingsAndSendApprovalEmail() {

        User user = BookingServiceImplTestData.user();
        Asset asset = BookingServiceImplTestData.assetWithApprovalRequired();
        RecurringBookingCreateDTO request = BookingServiceImplTestData.recurringCreateRequest();

        Booking booking1 = BookingServiceImplTestData.booking(user, asset);
        Booking booking2 = BookingServiceImplTestData.booking(user, asset);
        booking2.setId(TestConstants.BOOKING_ID_2);
        booking2.setBookingStart(TestConstants.START_2);
        booking2.setBookingEnd(TestConstants.END_2);

        when(userService.getActiveOrStudentUserById(USER_ID)).thenReturn(user);
        when(assetService.getActiveAssetById(ASSET_ID)).thenReturn(asset);
        when(securityService.isAdmin()).thenReturn(false);

        when(repository.saveAll(any())).thenReturn(List.of(booking1, booking2));
        when(mapper.toResponse(booking1)).thenReturn(BookingServiceImplTestData.response());
        when(mapper.toResponse(booking2)).thenReturn(BookingServiceImplTestData.approvedResponse());

        service.createRecurringBookings(request);

        verify(emailService).sendApprovalEmail(any(), any(), any(), any());
    }

    // ──────────────────────────────────────────────
    // approveBooking
    // ──────────────────────────────────────────────

    @Test
    void shouldApproveBooking() {

        User user = BookingServiceImplTestData.user();
        Asset asset = BookingServiceImplTestData.asset();
        Booking booking = BookingServiceImplTestData.booking(user, asset);

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(BookingServiceImplTestData.approvedResponse());

        BookingResponseDTO result = service.approveBooking(BOOKING_ID);

        assertEquals(BookingStatusEnum.APPROVED, result.status());
        verify(emailService).sendStatusNotificationEmail(
                user.getEmail(), asset.getName(), BookingStatusEnum.APPROVED.name());
    }

    @Test
    void shouldThrowExceptionWhenApprovingNonPendingBooking() {

        Booking booking = BookingServiceImplTestData.booking(
                BookingServiceImplTestData.user(),
                BookingServiceImplTestData.asset()
        );
        booking.setStatus(BookingStatusEnum.APPROVED);

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));

        assertThrows(IllegalStateException.class,
                () -> service.approveBooking(BOOKING_ID));

        verify(repository, never()).save(any());
    }

    // ──────────────────────────────────────────────
    // rejectBooking
    // ──────────────────────────────────────────────

    @Test
    void shouldRejectBooking() {

        User user = BookingServiceImplTestData.user();
        Asset asset = BookingServiceImplTestData.asset();
        Booking booking = BookingServiceImplTestData.booking(user, asset);

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(BookingServiceImplTestData.rejectedResponse());

        BookingResponseDTO result = service.rejectBooking(BOOKING_ID);

        assertEquals(BookingStatusEnum.REJECTED, result.status());
        verify(emailService).sendStatusNotificationEmail(
                user.getEmail(), asset.getName(), BookingStatusEnum.REJECTED.name());
    }

    @Test
    void shouldThrowExceptionWhenRejectingNonPendingBooking() {

        Booking booking = BookingServiceImplTestData.booking(
                BookingServiceImplTestData.user(),
                BookingServiceImplTestData.asset()
        );
        booking.setStatus(BookingStatusEnum.APPROVED);

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));

        assertThrows(IllegalStateException.class,
                () -> service.rejectBooking(BOOKING_ID));

        verify(repository, never()).save(any());
    }

    // ──────────────────────────────────────────────
    // bookingStatusToCompleted
    // ──────────────────────────────────────────────

    @Test
    void shouldUpdateCompletedBookings() {

        when(repository.updateCompletedBookings(any())).thenReturn(3);

        int count = service.bookingStatusToCompleted();

        assertEquals(3, count);
        verify(repository).updateCompletedBookings(any());
    }

    // ──────────────────────────────────────────────
    // getGeneralReport
    // ──────────────────────────────────────────────

    @Test
    void shouldGetGeneralReport() {

        GeneralReportProjection stats = mock(GeneralReportProjection.class);
        when(stats.getTotalBookingsCount()).thenReturn(10L);
        when(stats.getTotalCompletedBookingCount()).thenReturn(5L);
        when(stats.getTotalCancelledBookingCount()).thenReturn(1L);
        when(stats.getTotalPendingBookingCount()).thenReturn(2L);
        when(stats.getTotalApprovedBookingCount()).thenReturn(1L);
        when(stats.getTotalRejectedBookingCount()).thenReturn(1L);

        TopUserBookingsProjection userProjection = mock(TopUserBookingsProjection.class);
        when(userProjection.getUserId()).thenReturn(1L);
        when(userProjection.getFullName()).thenReturn("John Doe");
        when(userProjection.getBookingCount()).thenReturn(7L);

        TopAssetBookingsProjection assetProjection = mock(TopAssetBookingsProjection.class);
        when(assetProjection.getAssetId()).thenReturn(1L);
        when(assetProjection.getAssetName()).thenReturn("Laptop");
        when(assetProjection.getBookingCount()).thenReturn(4L);

        MonthlyBookingStatsProjection monthlyProjection = mock(MonthlyBookingStatsProjection.class);
        when(monthlyProjection.getYear()).thenReturn(2026);
        when(monthlyProjection.getMonth()).thenReturn(4);
        when(monthlyProjection.getTotalBookingsCount()).thenReturn(10L);
        when(monthlyProjection.getTotalCompletedBookingCount()).thenReturn(5L);
        when(monthlyProjection.getTotalCancelledBookingCount()).thenReturn(1L);
        when(monthlyProjection.getTotalPendingBookingCount()).thenReturn(2L);
        when(monthlyProjection.getTotalApprovedBookingCount()).thenReturn(1L);
        when(monthlyProjection.getTotalRejectedBookingCount()).thenReturn(1L);

        ReportFilter filter = new ReportFilter();
        filter.setFromDate(TestConstants.BASE_NOW);
        filter.setToDate(TestConstants.BASE_NOW.plusSeconds(86400));

        when(repository.getGeneralStats(any(), any(), any(), any())).thenReturn(stats);
        when(repository.getTopUsers(any(), any(), any())).thenReturn(List.of(userProjection));
        when(repository.getTopAssets(any(), any(), any())).thenReturn(List.of(assetProjection));
        when(repository.getMonthlyStats(any(), any(), any(), any())).thenReturn(List.of(monthlyProjection));

        GeneralReportResponseDTO actual = service.getGeneralReport(filter);

        assertNotNull(actual);
        assertEquals(10L, actual.totalBookingsCount());
        assertEquals(1, actual.topUsers().size());
        assertEquals(1, actual.topAssets().size());
        assertEquals(1, actual.monthlyStats().size());

        verify(repository).getGeneralStats(any(), any(), any(), any());
        verify(repository).getTopUsers(any(), any(), any());
        verify(repository).getTopAssets(any(), any(), any());
        verify(repository).getMonthlyStats(any(), any(), any(), any());
    }
}