package de.bdr.asset.management.user.department;

import de.bdr.asset.management.user.department.dtos.DepartmentRequestDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentResponseDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentUpdateRequestDTO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
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
    public ResponseEntity<DepartmentResponseDTO> create(
            @Valid @RequestBody DepartmentRequestDTO request
    ) throws DuplicateResourceException, ResourceNotFoundException
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createDepartment(request));
    }

    /** READ ALL */
    @Operation(summary = "Read list of departments", description = "Avaiable to authenticated users")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<DepartmentResponseDTO>> getAll(
            @ParameterObject Pageable pageable
    ) throws IllegalArgumentException
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllDepartments(pageable));
    }

    /** READ BY ID */
    @Operation(summary = "Read department by ID", description = "Available to anyone.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getById(
            @PathVariable Long id
    ) throws ResourceNotFoundException
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getDepartmentById(id));
    }

    /** UPDATE */
    @Operation(summary = "Update department details", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody DepartmentUpdateRequestDTO request
    ) throws ResourceNotFoundException, DuplicateResourceException
    {
        DepartmentResponseDTO updatedDepartment = service.updateDepartment(id, request);
        return ResponseEntity.ok(updatedDepartment);
    }
}
