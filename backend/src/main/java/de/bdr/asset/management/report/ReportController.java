package de.bdr.asset.management.report;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.bdr.asset.management.booking.BookingService;
import de.bdr.asset.management.report.dto.GeneralReportResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("v1/reports")
@Tag(
    name = "Reports",
    description = "Endpoints for Reports. ReportController"
)
public class ReportController {
    
    private final BookingService bookingService;

    public ReportController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /** GENERAL REPORT */
    @Operation(summary = "Get general report for bookings", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<GeneralReportResponseDTO> getGeneralReport (
        @ModelAttribute ReportFilter filter
    ) {
        return ResponseEntity.ok(bookingService.getGeneralReport(filter));
    }
}
