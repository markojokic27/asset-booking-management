package de.bdr.asset.management.user.department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** JPA Department Repository. */
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /** Assesses whether a department with the specified name already exists. */
    boolean existsByName(DepartmentEnum name);

    /** Assesses whether a conflicting department name exists assigned to a different identity key. */
    boolean existsByNameAndIdNot(DepartmentEnum name, Long id);

    /** Assesses whether a user is already assigned as a manager to any existing department. */
    boolean existsByManagerId(Long managerId);

    /** Assesses whether a conflicting manager assignment exists inside a different department. */
    boolean existsByManagerIdAndIdNot(Long managerId, Long departmentId);

    /** Retrieves a department by identity ID, pre-fetching the managing user via an entity graph join. */
    @EntityGraph(attributePaths = {"manager"})
    Optional<Department> findById(Long id);

    /** Fetches a paginated matrix of all departments, pre-fetching managing users to optimize query performance. */
    @EntityGraph(attributePaths = {"manager"})
    Page<Department> findAll(Pageable pageable);
}
