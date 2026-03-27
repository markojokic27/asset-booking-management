package de.bdr.asset.management.booking;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    /** CREATE */
    @PostMapping
    public ResponseEntity<BookingDTO> create(@Valid @RequestBody BookingDTO request) {

        BookingDTO createdBooking = service.createBooking(request);

        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    /** READ BY ID */
    @GetMapping("/{d}")
    public ResponseEntity<BookingDTO> getById(@PathVariable Long id) {

        BookingDTO foundBooking = service.getBookingById(id);

        return ResponseEntity.ok(foundBooking);
    }

    /** READ ALL */
    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAll() {

        List<BookingDTO> allBookings = service.getAllBookings();

        return ResponseEntity.ok(allBookings);
    }

    /** UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> update(@PathVariable Long id, @Valid @RequestBody BookingDTO request) {

        BookingDTO updatedBooking = service.updateBooking(id, request);

        return ResponseEntity.ok(updatedBooking);
    }

    /** Soft DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }

}
