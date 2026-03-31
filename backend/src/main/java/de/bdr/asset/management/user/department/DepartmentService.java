package de.bdr.asset.management.user.department;

import java.util.List;

/**
 * JPA Booking Repository
 */
public interface DepartmentService {

    /** CREATE */
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO request);

    /** READ */
    DepartmentResponseDTO getDepartmentById(Long id);
    List<DepartmentResponseDTO> getAllDepartments();

    /** UPDATE */
    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request);

    /** DELETE (Soft) */
    void deleteDepartment(Long id);
}
