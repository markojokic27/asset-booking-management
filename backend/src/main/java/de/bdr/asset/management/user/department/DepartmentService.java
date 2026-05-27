package de.bdr.asset.management.user.department;

import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.department.dtos.DepartmentRequestDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentResponseDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Application boundary interface for orchestrating department operations. */
public interface DepartmentService {

    /**
     * Registers a new department.
     *
     * @param request Data blueprint containing parameters to provision the department.
     * @return Data view representing the summary of the newly created department.
     * @throws DuplicateResourceException If the requested department name is already taken.
     * @throws ResourceNotFoundException If the designated initial manager ID does not exist.
     */
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO request);

    /**
     * Retrieves an individual department profile by ID.
     *
     * @param id Unique identity index lookup key.
     * @return Data view representing the requested department summary.
     * @throws ResourceNotFoundException If the specified department identity is missing.
     */
    DepartmentResponseDTO getDepartmentById(Long id);

    /**
     * Fetches a paginated list of all departments.
     *
     * @param pageable Pagination and sorting criteria matrix.
     * @return A paginated data window containing matching outbound department profiles.
     */
    Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable);

    /**
     * Updates details for an existing department.
     *
     * @param id Unique identity index lookup key of the target department.
     * @param request Data contract containing fields permitted for structural updates.
     * @return Data view representing the updated department summary.
     * @throws ResourceNotFoundException If the targeted department identity does not exist.
     * @throws DuplicateResourceException If the updated name or manager conflict with another department.
     */
    DepartmentResponseDTO updateDepartment(Long id, DepartmentUpdateRequestDTO request);
}
