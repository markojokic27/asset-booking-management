package de.bdr.asset.management.booking;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Tag(
        name = "Bookings",
        description = "Endpoints for Bookings."
)
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
    public ResponseEntity<Page<BookingResponseDTO>> getAll(
            @ParameterObject Pageable pageable
    ) {
        log.info("Received GET request to fetch bookings with pagination: " +
                        "Page number: {} | Page size: {} | Sort: {}",
                        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()
        );

        Page<BookingResponseDTO> allBookings = service.getAllBookings(pageable);

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
