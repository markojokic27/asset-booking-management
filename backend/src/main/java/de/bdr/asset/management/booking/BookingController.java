package de.bdr.asset.management.booking;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;
import de.bdr.asset.management.booking.dto.RecurringBookingCreateDTO;
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

    /** CREATE SINGLE*/
    @Operation(summary = "Create a booking", description = "Only available to authenticated users.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(
            @Valid @RequestBody BookingCreateDTO request
    ) throws InvalidDateRangeException, ResourceNotFoundException, DuplicateResourceException
    {
        BookingResponseDTO createdBooking = service.createBooking(request);

        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    /** CREATE RECURRING*/
    @Operation(summary = "Create recurring bookings", description = "Only available to authenticated users.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("@benefitEvaluator.canBook(authentication, #request.assetId)")
    @PostMapping("/recurring")
    public ResponseEntity<List<BookingResponseDTO>> createRecurring(
            @Valid @RequestBody RecurringBookingCreateDTO request
    ) throws InvalidDateRangeException, ResourceNotFoundException, DuplicateResourceException
    {
        List<BookingResponseDTO> createdBookings = service.createRecurringBookings(request);

        return new ResponseEntity<>(createdBookings, HttpStatus.CREATED);
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
        BookingResponseDTO foundBooking = service.getBookingById(id);

        return ResponseEntity.ok(foundBooking);
    }

    /** READ ALL */
    @Operation(summary = "Read list of bookings", description = "Only available to authenticated users.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<BookingResponseDTO>> getAll(
            @ModelAttribute BookingFilter filter,
            @ParameterObject Pageable pageable
    ) throws IllegalArgumentException
    {
        Page<BookingResponseDTO> allBookings = service.getAllBookings(filter, pageable);

        return ResponseEntity.ok(allBookings);
    }

    /** UPDATE */
    @Operation(summary = "Update booking", description = "Only available to authenticated users and users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("@bookingAuth.canUpdateBooking(authentication, #id)")
    @PatchMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody BookingUpdateDTO request
    ) throws ResourceNotFoundException, ActionNotAllowedException, InvalidDateRangeException, DuplicateResourceException
    {
        BookingResponseDTO updatedBooking = service.updateBooking(id, request);

        return ResponseEntity.ok(updatedBooking);
    }

    /** APPROVE */
    @Operation(summary = "Approve a pending booking", description = "Only available to the manager of the user who created the booking, or an ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("@bookingAuth.canManageBooking(authentication, #id)")
    @PostMapping("/{id}/approve")
    public ResponseEntity<BookingResponseDTO> approve(
            @PathVariable Long id
    ) throws ResourceNotFoundException, IllegalStateException, AccessDeniedException
    {
        BookingResponseDTO approvedBooking = service.approveBooking(id);

        return ResponseEntity.ok(approvedBooking);
    }

    /** REJECT */
    @Operation(summary = "Reject a pending booking", description = "Only available to the manager of the user who created the booking, or an ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("@bookingAuth.canManageBooking(authentication, #id)")
    @PostMapping("/{id}/reject")
    public ResponseEntity<BookingResponseDTO> reject(
            @PathVariable Long id
    ) throws ResourceNotFoundException, IllegalStateException, AccessDeniedException
    {
        BookingResponseDTO rejectedBooking = service.rejectBooking(id);

        return ResponseEntity.ok(rejectedBooking);
    }
}
