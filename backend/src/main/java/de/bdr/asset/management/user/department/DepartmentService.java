package de.bdr.asset.management.user.department;

import java.util.List;

/**
 * JPA Booking Repository
 */
public interface DepartmentService {

    /** CREATE */
    DepartmentRequestDTO createDepartment(DepartmentRequestDTO request);

    /** READ */
    DepartmentRequestDTO getDepartmentById(Long id);
    List<DepartmentRequestDTO> getAllDepartments();

    /** UPDATE */
    DepartmentRequestDTO updateDepartment(Long id, DepartmentRequestDTO request);

    /** DELETE (Soft) */
    void deleteDepartment(Long id);
}
