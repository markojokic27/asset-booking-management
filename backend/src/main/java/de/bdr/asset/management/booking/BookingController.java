package de.bdr.asset.management.booking;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Booking Controller
 */
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

        BookingResponseDTO createdBooking = service.createBooking(request);

        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    /** READ BY ID */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getById(@PathVariable Long id) {

        BookingResponseDTO foundBooking = service.getBookingById(id);

        return ResponseEntity.ok(foundBooking);
    }

    /** READ ALL */
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAll() {

        List<BookingResponseDTO> allBookings = service.getAllBookings();

        return ResponseEntity.ok(allBookings);
    }

    /** UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BookingRequestDTO request) {

        BookingResponseDTO updatedBooking = service.updateBooking(id, request);

        return ResponseEntity.ok(updatedBooking);
    }

    /** Soft DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }

}
