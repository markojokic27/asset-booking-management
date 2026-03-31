package de.bdr.asset.management.user.department;

import java.util.List;

/**
 * JPA Booking Repository
 */
public interface DepartmentService {

    /** CREATE */
    DepartmentDTO createDepartment(DepartmentDTO request);

    /** READ */
    DepartmentDTO getDepartmentById(Long id);
    List<DepartmentDTO> getAllDepartments();

    /** UPDATE */
    DepartmentDTO updateDepartment(Long id, DepartmentDTO request);

    /** DELETE (Soft) */
    void deleteDepartment(Long id);
}
