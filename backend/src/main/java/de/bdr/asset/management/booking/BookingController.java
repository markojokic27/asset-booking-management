package de.bdr.asset.management.booking;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.core.exception.ActionNotAllowedException;
import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.InvalidDateRangeException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Booking Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/bookings")
@Tag(
        name = "Bookings",
        description = "Endpoints for Bookings. BookingController"
)
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    /** CREATE */
    @Operation(summary = "Create a booking", description = "Only available to authenticated users.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(
            @Valid @RequestBody BookingCreateDTO request
    ) throws InvalidDateRangeException, ResourceNotFoundException, DuplicateResourceException
    {
        log.info("Received POST request to create a new booking");

        BookingResponseDTO createdBooking = service.createBooking(request);

        log.debug("Successfully processed POST request for creating new booking");

        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    /** READ BY ID */
    @Operation(summary = "Read booking by ID", description = "Only available to authenticated users.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getById(
            @PathVariable Long id
    ) throws ResourceNotFoundException
    {
        log.info("Received GET request to fetch booking with id: {}", id);

        BookingResponseDTO foundBooking = service.getBookingById(id);

        log.debug("Successfully processed GET request for booking id: {}", id);

        return ResponseEntity.ok(foundBooking);
    }

    /** READ ALL */
    @Operation(summary = "Read list of bookings", description = "Only available to authenticated users.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<BookingResponseDTO>> getAll(
            @ParameterObject Pageable pageable
    ) throws IllegalArgumentException
    {
        log.info("Received GET request to fetch bookings with pagination: " +
                        "Page number: {} | Page size: {} | Sort: {}",
                        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()
        );

        Page<BookingResponseDTO> allBookings = service.getAllBookings(pageable);

        log.debug("Successfully processed GET request for all booking");

        return ResponseEntity.ok(allBookings);
    }

    /** UPDATE */
    @Operation(summary = "Update booking", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody BookingUpdateDTO request
    ) throws ResourceNotFoundException, ActionNotAllowedException, InvalidDateRangeException, DuplicateResourceException
    {
        log.info("Received PUT request to update booking with id: {}", id);

        BookingResponseDTO updatedBooking = service.updateBooking(id, request);

        log.debug("Successfully processed PUT request for booking id: {}", id);

        return ResponseEntity.ok(updatedBooking);
    }

    /** Soft DELETE */
    @Operation(summary = "Soft delete booking", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    )
    {
        log.info("Received DELETE request for booking with id: {}", id);

        service.deleteBooking(id);

        log.debug("Successfully processed DELETE request for booking id: {}", id);

        return ResponseEntity.noContent().build();
    }

}
