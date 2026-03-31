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
@RequestMapping("api/v1/departments")
public class DepartmentController {
    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<DepartmentDTO> create(@Valid @RequestBody DepartmentDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createDepartment(request));
    }
    // READ
    // ALL
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllDepartments());
    }

    // BY ID
    @GetMapping("/{id}")
    ResponseEntity<DepartmentDTO> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getDepartmentById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    ResponseEntity<DepartmentDTO> update(@PathVariable Long id, @Valid @RequestBody DepartmentDTO request) {
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
