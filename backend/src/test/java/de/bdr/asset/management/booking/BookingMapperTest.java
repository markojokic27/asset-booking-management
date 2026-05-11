package de.bdr.asset.management.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;

import static de.bdr.asset.management.booking.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    private BookingMapper bookingMapper;

    @BeforeEach
    void setUp() {
        bookingMapper = new BookingMapperImpl();
    }

    // --- toEntity ---

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        Booking result = bookingMapper.toEntity(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldMapToEntityCorrectly() {
        Booking result = bookingMapper.toEntity(BookingMapperTestData.createRequest(true));

        assertThat(result.getBookingStart()).isEqualTo(START);
        assertThat(result.getBookingEnd()).isEqualTo(END);
        assertThat(result.getNotes()).isEqualTo(NOTES_DATA);

        assertThat(result.getId()).isNull();
        assertThat(result.getUser()).isNull();
        assertThat(result.getAsset()).isNull();
        assertThat(result.getStatus()).isNull();
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getLastModifiedAt()).isNull();
    }

    @Test
    void shouldMapNullNotesToEntity() {
        BookingCreateDTO request = BookingMapperTestData.createRequest(false);

        Booking result = bookingMapper.toEntity(request);

        assertThat(result.getNotes()).isNull();
    }

    // --- toResponse ---

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        BookingResponseDTO result = bookingMapper.toResponse(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldMapToResponseWithPopulatedRelations() {

        Booking result = BookingMapperTestData.buildBookingWithRelations();

        BookingResponseDTO dto = bookingMapper.toResponse(result);

        assertThat(dto.id()).isEqualTo(BOOKING_ID);
        assertThat(dto.status()).isEqualTo(BookingStatusEnum.ACTIVE);

        assertThat(dto.user()).isNotNull();
        assertThat(dto.user().id()).isEqualTo(USER_ID);
        assertThat(dto.user().username()).isEqualTo(USER_NAME);
        assertThat(dto.user().role()).isEqualTo(USER_ROLE);

        assertThat(dto.asset()).isNotNull();
        assertThat(dto.asset().name()).isEqualTo(ASSET_NAME);
        assertThat(dto.asset().status()).isEqualTo(ASSET_STATUS);
        assertThat(dto.asset().category()).isEqualTo(CATEGORY_NAME);
    }

    @Test
    void shouldHandleNullRelationsInResponse() {

        Booking booking = BookingMapperTestData.buildBookingWithNullRelations();

        BookingResponseDTO dto = bookingMapper.toResponse(booking);

        assertThat(dto.user()).isNull();
        assertThat(dto.asset()).isNull();
        assertThat(dto.notes()).isEqualTo(NOTES_DATA);
    }
}