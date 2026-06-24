package de.bdr.asset.management.booking;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.booking.dto.RecurringBookingCreateDTO;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    /** CREATE */
    @Test
    void createBooking_validRequest_returnsCreatedStatus(){
        BookingCreateDTO request = BookingControllerTestData.createRequest();
        BookingResponseDTO response = BookingControllerTestData.response();

        when(bookingService.createBooking(request)).thenReturn(response);

        ResponseEntity<BookingResponseDTO> result = bookingController.create(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        verify(bookingService).createBooking(request);

    }

    /** READ ALL */
    @Test
    void getAllBookings_returnsOkWithLIst(){
        BookingResponseDTO response = BookingControllerTestData.response();

        List<BookingResponseDTO> list = List.of(response);
        Page<BookingResponseDTO> page = new PageImpl<>(list);

        when(bookingService.getAllBookings(any(BookingFilter.class), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<BookingResponseDTO>> result =
                bookingController.getAll(new BookingFilter(), PageRequest.of(0, 10));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert(result.getBody() != null);
        assertThat(result.getBody().getContent())
                .hasSize(1)
                .contains(response);
    }

    /** READ BY ID */
    @Test
    void getBookingById_returnsOkWithBooking(){
        BookingResponseDTO response = BookingControllerTestData.response();

        when(bookingService.getBookingById(1L)).thenReturn(response);

        ResponseEntity<BookingResponseDTO> result = bookingController.getById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** UPDATE */
    @Test
    void updateBooking_returnsOkWithUpdatesdBooking(){
        BookingUpdateDTO request = BookingControllerTestData.updateRequest();
        BookingResponseDTO response = BookingControllerTestData.response();

        when(bookingService.updateBooking(1L, request)).thenReturn(response);

        ResponseEntity<BookingResponseDTO> result = bookingController.update(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** CREATE RECURRING */
    @Test
    void createRecurring_returnsCreated() {
        RecurringBookingCreateDTO request = BookingControllerTestData.recurringCreateRequest();
        List<BookingResponseDTO> responses = List.of(BookingControllerTestData.response());

        when(bookingService.createRecurringBookings(request)).thenReturn(responses);

        ResponseEntity<List<BookingResponseDTO>> result = bookingController.createRecurring(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).hasSize(1);
        verify(bookingService).createRecurringBookings(request);
    }

    /** APPROVE */
    @Test
    void approve_returnsOk() {
        BookingResponseDTO response = BookingControllerTestData.approvedResponse();

        when(bookingService.approveBooking(1L)).thenReturn(response);

        ResponseEntity<BookingResponseDTO> result = bookingController.approve(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(bookingService).approveBooking(1L);
    }

    /** REJECT */
    @Test
    void reject_returnsOk() {
        BookingResponseDTO response = BookingControllerTestData.rejectedResponse();

        when(bookingService.rejectBooking(1L)).thenReturn(response);

        ResponseEntity<BookingResponseDTO> result = bookingController.reject(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(bookingService).rejectBooking(1L);
    }
}
