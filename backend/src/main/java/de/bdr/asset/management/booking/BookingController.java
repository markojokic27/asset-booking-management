package de.bdr.asset.management.booking;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * Booking Controller
 */
@RestController
@RequestMapping("api/v1/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    /** CREATE */
    @PostMapping
    public ResponseEntity<BookingRequestDTO> create(@Valid @RequestBody BookingRequestDTO request) {

        BookingRequestDTO createdBooking = service.createBooking(request);

        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    /** READ BY ID */
    @GetMapping("/{id}")
    public ResponseEntity<BookingRequestDTO> getById(@PathVariable Long id) {

        BookingRequestDTO foundBooking = service.getBookingById(id);

        return ResponseEntity.ok(foundBooking);
    }

    /** READ ALL */
    @GetMapping
    public ResponseEntity<List<BookingRequestDTO>> getAll() {

        List<BookingRequestDTO> allBookings = service.getAllBookings();

        return ResponseEntity.ok(allBookings);
    }

    /** UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<BookingRequestDTO> update(@PathVariable Long id, @Valid @RequestBody BookingRequestDTO request) {

        BookingRequestDTO updatedBooking = service.updateBooking(id, request);

        return ResponseEntity.ok(updatedBooking);
    }

    /** Soft DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }

}
