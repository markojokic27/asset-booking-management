// package de.bdr.asset.management.booking;

// import de.bdr.asset.management.asset.Asset;
// import de.bdr.asset.management.user.User;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import java.time.Instant;

// import static org.assertj.core.api.Assertions.assertThat;

// public class BookingMapperTest {

//     private BookingMapper bookingMapper;

//     @BeforeEach
//     void setUp() {
//         bookingMapper = new BookingMapperImpl();
//     }

//     private final Instant bookingStart = Instant.parse("2025-01-01T08:00:00Z");
//     private final Instant bookingEnd = Instant.parse("2025-01-01T18:00:00Z");

//     private BookingRequestDTO buildRequest() {
//         return new BookingRequestDTO(
//                 1L,
//                 2L,
//                 BookingStatusEnum.PENDING,
//                 bookingStart,
//                 bookingEnd,
//                 "Some notes"
//         );
//     }

//     private Booking buildBooking() {
//         User user = new User();
//         user.setId(1L);

//         Asset asset = new Asset();
//         asset.setId(2L);

//         Booking booking = new Booking();
//         booking.setId(10L);
//         booking.setUser(user);
//         booking.setAsset(asset);
//         booking.setStatus(BookingStatusEnum.PENDING);
//         booking.setBookingStart(bookingStart);
//         booking.setBookingEnd(bookingEnd);
//         booking.setNotes("Some notes");
//         return booking;
//     }

//     // --- toEntity ---

//     @Test
//     void shouldReturnNullWhenRequestIsNull() {
//         Booking result = bookingMapper.toEntity(null);
//         assertThat(result).isNull();
//     }

//     @Test
//     void shouldMapStatusToEntity() {
//         Booking result = bookingMapper.toEntity(buildRequest());
//         assertThat(result.getStatus()).isEqualTo(BookingStatusEnum.PENDING);
//     }

//     @Test
//     void shouldMapBookingStartToEntity() {
//         Booking result = bookingMapper.toEntity(buildRequest());
//         assertThat(result.getBookingStart()).isEqualTo(bookingStart);
//     }

//     @Test
//     void shouldMapBookingEndToEntity() {
//         Booking result = bookingMapper.toEntity(buildRequest());
//         assertThat(result.getBookingEnd()).isEqualTo(bookingEnd);
//     }

//     @Test
//     void shouldMapNotesToEntity() {
//         Booking result = bookingMapper.toEntity(buildRequest());
//         assertThat(result.getNotes()).isEqualTo("Some notes");
//     }

//     @Test
//     void shouldMapNullNotesToEntity() {
//         BookingRequestDTO request = new BookingRequestDTO(
//                 1L, 2L, BookingStatusEnum.PENDING, bookingStart, bookingEnd, null
//         );
//         Booking result = bookingMapper.toEntity(request);
//         assertThat(result.getNotes()).isNull();
//     }

//     @Test
//     void shouldIgnoreIdWhenMappingToEntity() {
//         Booking result = bookingMapper.toEntity(buildRequest());
//         assertThat(result.getId()).isNull();
//     }

//     @Test
//     void shouldIgnoreUserWhenMappingToEntity() {
//         Booking result = bookingMapper.toEntity(buildRequest());
//         assertThat(result.getUser()).isNull();
//     }

//     @Test
//     void shouldIgnoreAssetWhenMappingToEntity() {
//         Booking result = bookingMapper.toEntity(buildRequest());
//         assertThat(result.getAsset()).isNull();
//     }

//     @Test
//     void shouldIgnoreCreatedAtWhenMappingToEntity() {
//         Booking result = bookingMapper.toEntity(buildRequest());
//         assertThat(result.getCreatedAt()).isNull();
//     }

//     @Test
//     void shouldIgnoreLastModifiedAtWhenMappingToEntity() {
//         Booking result = bookingMapper.toEntity(buildRequest());
//         assertThat(result.getLastModifiedAt()).isNull();
//     }

//     // --- toResponse ---

//     @Test
//     void shouldReturnNullWhenEntityIsNull() {
//         BookingResponseDTO result = bookingMapper.toResponse(null);
//         assertThat(result).isNull();
//     }

//     @Test
//     void shouldMapIdToResponse() {
//         BookingResponseDTO result = bookingMapper.toResponse(buildBooking());
//         assertThat(result.id()).isEqualTo(10L);
//     }

//     @Test
//     void shouldMapUserIdFromNestedUser() {
//         BookingResponseDTO result = bookingMapper.toResponse(buildBooking());
//         assertThat(result.userId()).isEqualTo(1L);
//     }

//     @Test
//     void shouldSetUserIdToNullWhenUserIsNull() {
//         Booking booking = buildBooking();
//         booking.setUser(null);

//         BookingResponseDTO result = bookingMapper.toResponse(booking);
//         assertThat(result.userId()).isNull();
//     }

//     @Test
//     void shouldMapAssetIdFromNestedAsset() {
//         BookingResponseDTO result = bookingMapper.toResponse(buildBooking());
//         assertThat(result.assetId()).isEqualTo(2L);
//     }

//     @Test
//     void shouldSetAssetIdToNullWhenAssetIsNull() {
//         Booking booking = buildBooking();
//         booking.setAsset(null);

//         BookingResponseDTO result = bookingMapper.toResponse(booking);
//         assertThat(result.assetId()).isNull();
//     }

//     @Test
//     void shouldMapStatusToResponse() {
//         BookingResponseDTO result = bookingMapper.toResponse(buildBooking());
//         assertThat(result.status()).isEqualTo(BookingStatusEnum.PENDING);
//     }

//     @Test
//     void shouldMapBookingStartToResponse() {
//         BookingResponseDTO result = bookingMapper.toResponse(buildBooking());
//         assertThat(result.bookingStart()).isEqualTo(bookingStart);
//     }

//     @Test
//     void shouldMapBookingEndToResponse() {
//         BookingResponseDTO result = bookingMapper.toResponse(buildBooking());
//         assertThat(result.bookingEnd()).isEqualTo(bookingEnd);
//     }

//     @Test
//     void shouldMapNotesToResponse() {
//         BookingResponseDTO result = bookingMapper.toResponse(buildBooking());
//         assertThat(result.notes()).isEqualTo("Some notes");
//     }

//     @Test
//     void shouldMapNullNotesToResponse() {
//         Booking booking = buildBooking();
//         booking.setNotes(null);

//         BookingResponseDTO result = bookingMapper.toResponse(booking);
//         assertThat(result.notes()).isNull();
//     }
// }