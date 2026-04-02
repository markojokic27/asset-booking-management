package de.bdr.asset.management.booking;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Booking Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    /** CREATE */
    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(@Valid @RequestBody BookingRequestDTO request) {
        log.info("Received POST request to create a new booking");

        BookingResponseDTO createdBooking = service.createBooking(request);

        log.debug("Successfully processed POST request for creating new booking");

        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    /** READ BY ID */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getById(@PathVariable Long id) {
        log.info("Received GET request to fetch booking with id: {}", id);

        BookingResponseDTO foundBooking = service.getBookingById(id);

        log.debug("Successfully processed GET request for booking id: {}", id);

        return ResponseEntity.ok(foundBooking);
    }

    /** READ ALL */
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAll() {
        log.info("Received GET request to fetch all bookings");

        List<BookingResponseDTO> allBookings = service.getAllBookings();

        log.debug("Successfully processed GET request for all booking");

        return ResponseEntity.ok(allBookings);
    }

    /** UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BookingRequestDTO request) {
        log.info("Received PUT request to update booking with id: {}", id);

        BookingResponseDTO updatedBooking = service.updateBooking(id, request);

        log.debug("Successfully processed PUT request for booking id: {}", id);

        return ResponseEntity.ok(updatedBooking);
    }

    /** Soft DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Received DELETE request for booking with id: {}", id);

        service.deleteBooking(id);

        log.debug("Successfully processed DELETE request for booking id: {}", id);

        return ResponseEntity.noContent().build();
    }

}
