package de.bdr.asset.management.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;

import static de.bdr.asset.management.booking.TestConstants.NOTES_DATA;

import static org.assertj.core.api.Assertions.assertThat;

import static de.bdr.asset.management.booking.TestConstants.BOOKING_ID;
import static de.bdr.asset.management.booking.TestConstants.END;
import static de.bdr.asset.management.booking.TestConstants.START;

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
    void shouldMapToResponseCorrectly() {
        Booking result = BookingMapperTestData.buildBookingWithNullRelations();

        BookingResponseDTO dto = bookingMapper.toResponse(result);

        assertThat(dto.id()).isEqualTo(BOOKING_ID);
        assertThat(dto.userId()).isNull();
        assertThat(dto.assetId()).isNull();
        assertThat(dto.status()).isEqualTo(BookingStatusEnum.ACTIVE);
        assertThat(dto.bookingStart()).isEqualTo(START);
        assertThat(dto.bookingEnd()).isEqualTo(END);
        assertThat(dto.notes()).isEqualTo(NOTES_DATA);
    }

    @Test
    void shouldHandleNullRelationsInResponse() {
        Booking booking = BookingMapperTestData.buildBookingWithNullRelations();
        booking.setUser(null);
        booking.setAsset(null);
        booking.setNotes(null);

        BookingResponseDTO dto = bookingMapper.toResponse(booking);

        assertThat(dto.userId()).isNull();
        assertThat(dto.assetId()).isNull();
        assertThat(dto.notes()).isNull();
    }
}