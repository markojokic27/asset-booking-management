package de.bdr.asset.management.user.department;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Department Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/departments")
@Tag(
        name = "Departments",
        description = "Endpoints for Departments. DepartmentController"
)
public class DepartmentController {
    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    /** CREATE */
    @Operation(summary = "Create Department", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> create(@Valid @RequestBody DepartmentRequestDTO request) {
        log.info("Received POST request to create a new department");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createDepartment(request));
    }

    /** READ ALL */
    // TODO: Discuss if authentication is necessary because of user registration
    @Operation(summary = "Read list of departments", description = "Avaiable to ...")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<DepartmentResponseDTO>> getAll(
            @ParameterObject Pageable pageable
    ) {
        log.info("Received GET request to fetch departments with pagination: " +
                        "Page number: {} | Page size: {} | Sort: {}",
                        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllDepartments(pageable));
    }

    /** READ BY ID */
    @Operation(summary = "Read department by ID", description = "Available to anyone.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getById(@PathVariable Long id) {
        log.info("Received GET request to fetch department with id: {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getDepartmentById(id));
    }

    /** UPDATE */
    @Operation(summary = "Update department details", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody DepartmentRequestDTO request) {
        log.info("Received PUT request to update department with id: {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateDepartment(id, request));
    }

    /** Soft DELETE */
    @Operation(summary = "Soft delete department", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Received DELETE request for department with id: {}", id);

        service.deleteDepartment(id);

        log.debug("Successfully processed DELETE request for department id: {}", id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
