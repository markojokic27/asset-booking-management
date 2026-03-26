package de.bdr.asset.management.booking;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
