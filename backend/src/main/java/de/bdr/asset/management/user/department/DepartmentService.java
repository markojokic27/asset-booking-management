package de.bdr.asset.management.user.department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * JPA Booking Repository
 */
public interface DepartmentService {

    /** CREATE */
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO request);

    /** READ */
    DepartmentResponseDTO getDepartmentById(Long id);
    Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable);

    /** UPDATE */
    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request);
}
