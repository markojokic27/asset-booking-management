package de.bdr.asset.management.user.department;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Department Controller
 */
@RestController
@RequestMapping("v1/departments")
public class DepartmentController {
    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> create(@Valid @RequestBody DepartmentRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createDepartment(request));
    }
    // READ
    // ALL
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllDepartments());
    }

    // BY ID
    @GetMapping("/{id}")
    ResponseEntity<DepartmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getDepartmentById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    ResponseEntity<DepartmentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody DepartmentRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateDepartment(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteDepartment(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
