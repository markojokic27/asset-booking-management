// package de.bdr.asset.management.booking;

// import java.time.Instant;
// import java.time.LocalDateTime;
// import java.time.ZoneOffset;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import static org.mockito.ArgumentMatchers.any;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;

// import de.bdr.asset.management.asset.Asset;
// import de.bdr.asset.management.asset.AssetRepository;
// import de.bdr.asset.management.core.exception.ResourceNotFoundException;
// import de.bdr.asset.management.user.User;
// import de.bdr.asset.management.user.UserRepository;

// @ExtendWith(MockitoExtension.class)
// class BookingServiceImplTest {

//     @Mock
//     private BookingRepository repository;

//     @Mock
//     private BookingMapper mapper;

//     @Mock
//     private UserRepository userRepository;

//     @Mock
//     private AssetRepository assetRepository;

//     @InjectMocks
//     private BookingServiceImpl service;

//     private Booking booking;
//     private User user;
//     private Asset asset;
//     private BookingRequestDTO requestDTO;
//     private BookingResponseDTO responseDTO;

//     @BeforeEach
//     void setUp() {
//         user = new User();
//         user.setId(1L);
//         user.setName("ivan ivic");

//         asset = new Asset();
//         asset.setId(1L);
//         asset.setName("Books");

//         Instant bookingStartInstant = LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC);
//         Instant bookingEndInstant = LocalDateTime.now().plusDays(1).plusHours(1).toInstant(ZoneOffset.UTC);

//         booking = new Booking();
//         booking.setId(1L);
//         booking.setUser(user);
//         booking.setAsset(asset);
//         booking.setStatus(BookingStatusEnum.ACTIVE);
//         booking.setBookingStart(bookingStartInstant);
//         booking.setBookingEnd(bookingEndInstant);
//         booking.setNotes("text");


//         requestDTO = new BookingRequestDTO(
//                 1L,
//                 1L,
//                 BookingStatusEnum.ACTIVE,
//                 bookingStartInstant,
//                 bookingEndInstant,
//                 "text"
//         );

//         responseDTO = new BookingResponseDTO(
//                 1L,
//                 1L,
//                 1L,
//                 BookingStatusEnum.ACTIVE,
//                 bookingStartInstant,
//                 bookingEndInstant,
//                 "text"
//         );
//     }

//     // Tests createBooking(): user and asset exist, booking saved
//     @Test
//     void shouldCreateBooking() {

//         when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//         when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
//         when(mapper.toEntity(requestDTO)).thenReturn(booking);
//         when(repository.save(booking)).thenReturn(booking);
//         when(mapper.toResponse(booking)).thenReturn(responseDTO);

//         BookingResponseDTO result = service.createBooking(requestDTO);

//         assertNotNull(result);
//         assertEquals(BookingStatusEnum.ACTIVE, result.status());

//         verify(repository).save(booking);
//         verify(mapper).toResponse(booking);
//     }

//     // Tests createBooking(): throws if user not found
//     @Test
//     void shouldThrowExceptionWhenUserNotFound() {

//         when(userRepository.findById(1L)).thenReturn(Optional.empty());

//         assertThrows(ResourceNotFoundException.class,
//                 () -> service.createBooking(requestDTO));

//         verify(repository, never()).save(any());
//     }

//     // Tests createBooking(): throws if asset not found
//     @Test
//     void shouldThrowExceptionWhenAssetNotFound() {

//         when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//         when(assetRepository.findById(1L)).thenReturn(Optional.empty());

//         assertThrows(ResourceNotFoundException.class,
//                 () -> service.createBooking(requestDTO));

//         verify(repository, never()).save(any());
//     }

//     // Tests getBookingById(): booking found
//     @Test
//     void shouldGetBookingById() {

//         when(repository.findById(1L)).thenReturn(Optional.of(booking));
//         when(mapper.toResponse(booking)).thenReturn(responseDTO);

//         BookingResponseDTO result = service.getBookingById(1L);

//         assertEquals(1L, result.id());

//         verify(repository).findById(1L);
//     }

//     // Tests getBookingById(): throws if not found
//     @Test
//     void shouldThrowExceptionWhenBookingNotFound() {

//         when(repository.findById(1L)).thenReturn(Optional.empty());

//         assertThrows(ResourceNotFoundException.class,
//                 () -> service.getBookingById(1L));
//     }

//     // Tests getAllBookings(): fetch all bookings
//     @Test
//     void shouldReturnAllBookings() {

//         Pageable pageable = PageRequest.of(0, 10);
//         Page<Booking> bookingPage = new PageImpl<>(java.util.List.of(booking));

//         when(repository.findAll(pageable)).thenReturn(bookingPage);
//         when(mapper.toResponse(booking)).thenReturn(responseDTO);

//         Page<BookingResponseDTO> result = service.getAllBookings(pageable);

//         assertEquals(1, result.getTotalElements());

//         verify(repository).findAll(pageable);
//     }

//     // Tests updateBooking(): booking exists, user and asset exist, update saved
//     @Test
//     void shouldUpdateBooking() {

//         when(repository.findById(1L)).thenReturn(Optional.of(booking));
//         when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//         when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
//         when(repository.save(booking)).thenReturn(booking);
//         when(mapper.toResponse(booking)).thenReturn(responseDTO);

//         BookingResponseDTO result = service.updateBooking(1L, requestDTO);

//         assertEquals(BookingStatusEnum.ACTIVE, result.status());
//         verify(repository).save(booking);
//     }

//     // Tests updateBooking(): throws if booking not found
//     @Test
//     void shouldThrowExceptionWhenUpdatingNonExistingBooking() {

//         when(repository.findById(1L)).thenReturn(Optional.empty());

//         assertThrows(ResourceNotFoundException.class,
//                 () -> service.updateBooking(1L, requestDTO));
//     }

//     // Tests updateBooking(): throws if user not found
//     @Test
//     void shouldThrowExceptionWhenUpdatingBookingWithNonExistingUser() {

//         when(repository.findById(1L)).thenReturn(Optional.of(booking));
//         when(userRepository.findById(1L)).thenReturn(Optional.empty());

//         assertThrows(ResourceNotFoundException.class,
//                 () -> service.updateBooking(1L, requestDTO));
//     }

//     // Tests updateBooking(): throws if asset not found
//     @Test
//     void shouldThrowExceptionWhenUpdatingBookingWithNonExistingAsset() {

//         when(repository.findById(1L)).thenReturn(Optional.of(booking));
//         when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//         when(assetRepository.findById(1L)).thenReturn(Optional.empty());

//         assertThrows(ResourceNotFoundException.class,
//                 () -> service.updateBooking(1L, requestDTO));
//     }
// }