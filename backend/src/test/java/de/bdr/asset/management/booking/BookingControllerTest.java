package de.bdr.asset.management.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    /** CREATE */
    @Test
    void createBooking_validRequest_returnsCreatedStatus(){
        BookingRequestDTO request=new BookingRequestDTO( 2L, 1L, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "text");
        BookingResponseDTO response=new BookingResponseDTO(  1L, 2L, 1L, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "text");

        when(bookingService.createBooking(request)).thenReturn(response);

        ResponseEntity<BookingResponseDTO> result = bookingController.create(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        verify(bookingService).createBooking(request);

    }

    /** READ ALL */
    @Test
    void getAllBookings_returnsOkWithLIst(){
        BookingResponseDTO response = new BookingResponseDTO(1L, 2L, 1L, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "text");

        List<BookingResponseDTO> list = List.of(response);
        Page<BookingResponseDTO> page = new PageImpl<>(list);

        when(bookingService.getAllBookings(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<BookingResponseDTO>> result =
                bookingController.getAll(PageRequest.of(0, 10));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert(result.getBody() != null);
        assertThat(result.getBody().getContent())
                .hasSize(1)
                .contains(response);
    }

    /** READ BY ID */
    @Test
    void getBookingById_returnsOkWithBooking(){
        BookingResponseDTO response=new BookingResponseDTO(  1L, 2L, 1L, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "text");

        when(bookingService.getBookingById(1L)).thenReturn(response);

        ResponseEntity<BookingResponseDTO> result = bookingController.getById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** UPDATE */
    @Test
    void updateBooking_returnsOkWithUpdatesdBooking(){
        BookingRequestDTO request=new BookingRequestDTO( 2L, 1L, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "text");
        BookingResponseDTO response=new BookingResponseDTO(  1L, 2L, 1L, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "text");

        when(bookingService.updateBooking(1L, request)).thenReturn(response);

        ResponseEntity<BookingResponseDTO> result = bookingController.update(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** DELETE */
    @Test
    void deleteAssetCategory_returnsNoContent() {

        ResponseEntity<Void> result = bookingController.delete(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(result.getBody()).isNull();
        verify(bookingService).deleteBooking(1L);
    }


}
