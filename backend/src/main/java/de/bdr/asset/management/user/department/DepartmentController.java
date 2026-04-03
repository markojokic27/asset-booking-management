package de.bdr.asset.management.user.department;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Department Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/departments")
public class DepartmentController {
    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    /** CREATE */
    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> create(@Valid @RequestBody DepartmentRequestDTO request) {
        log.info("Received POST request to create a new department");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createDepartment(request));
    }

    /** READ ALL */
    @GetMapping
    public ResponseEntity<Page<DepartmentResponseDTO>> getAll(
        Pageable pageable
    ) {
        log.info("Received GET request to fetch all departments");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllDepartments(pageable));
    }

    /** READ BY ID */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getById(@PathVariable Long id) {
        log.info("Received GET request to fetch department with id: {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getDepartmentById(id));
    }

    /** UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody DepartmentRequestDTO request) {
        log.info("Received PUT request to update department with id: {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateDepartment(id, request));
    }

    /** Soft DELETE */
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
