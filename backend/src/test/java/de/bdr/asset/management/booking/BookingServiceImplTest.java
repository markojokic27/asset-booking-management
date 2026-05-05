package de.bdr.asset.management.booking;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import de.bdr.asset.management.asset.AssetRepository;
import de.bdr.asset.management.asset.AssetStatusEnum;
import static de.bdr.asset.management.booking.TestConstants.ASSET_ID;
import static de.bdr.asset.management.booking.TestConstants.BOOKING_ID;
import static de.bdr.asset.management.booking.TestConstants.USER_ID;
import static de.bdr.asset.management.booking.TestConstants.validUpdateUserStatuses;
import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.core.security.SecurityService;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository repository;

    @Mock
    private BookingMapper mapper;

    @Mock
    private SecurityService securityService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssetRepository assetRepository;

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
                userRepository,
                assetRepository,
                securityService,
                fixedClock
        );
    }

    private void mockAdminUser() {
        when(securityService.isAdmin()).thenReturn(true);
        when(securityService.getCurrentUserId()).thenReturn(USER_ID);
    }

    // Tests createBooking(): user and asset exist, booking saved
    @Test
    void shouldCreateBooking() {
        mockAdminUser();

        User user = BookingServiceImplTestData.user();
        Asset asset = BookingServiceImplTestData.asset();
        Booking booking = BookingServiceImplTestData.booking(user, asset);

        BookingCreateDTO request = BookingServiceImplTestData.createRequest();
        BookingResponseDTO response = BookingServiceImplTestData.response();

        when(userRepository.findByIdAndStatusIn(USER_ID, validUpdateUserStatuses)).thenReturn(Optional.of(user));
        when(assetRepository.findByIdAndStatus(ASSET_ID, AssetStatusEnum.ACTIVE)).thenReturn(Optional.of(asset));
        when(mapper.toEntity(request)).thenReturn(booking);
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(response);

        BookingResponseDTO result = service.createBooking(request);

        assertEquals(response, result);
    }

    // Tests createBooking(): throws if user not found
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        mockAdminUser();

        BookingCreateDTO request = BookingServiceImplTestData.createRequest();

        when(userRepository.findByIdAndStatusIn(USER_ID, validUpdateUserStatuses)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createBooking(request));

        verify(repository, never()).save(any());
    }

    // Tests createBooking(): throws if asset not found
    @Test
    void shouldThrowExceptionWhenAssetNotFound() {
        mockAdminUser();

        User user = BookingServiceImplTestData.user();

        BookingCreateDTO request = BookingServiceImplTestData.createRequest();

        when(userRepository.findByIdAndStatusIn(USER_ID, validUpdateUserStatuses)).thenReturn(Optional.of(user));
        when(assetRepository.findByIdAndStatus(ASSET_ID, AssetStatusEnum.ACTIVE)).thenReturn(Optional.empty());

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
        Page<Booking> bookingPage = new PageImpl<>(java.util.List.of(booking));

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

        assertEquals(BookingStatusEnum.ACTIVE, result.status());
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
}