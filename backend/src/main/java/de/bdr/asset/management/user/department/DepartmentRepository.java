package de.bdr.asset.management.user.department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA Department Repository
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByName(DepartmentEnum name);

    boolean existsByNameAndIdNot(DepartmentEnum name, Long id);

    boolean existsByManagerId(Long managerId);

    boolean existsByManagerIdAndIdNot(Long managerId, Long departmentId);

    @EntityGraph(attributePaths = {"manager"})
    Optional<Department> findById(Long id);

    @EntityGraph(attributePaths = {"manager"})
    Optional<Department> findByName(DepartmentEnum name);

    @EntityGraph(attributePaths = {"manager"})
    Page<Department> findAll(Pageable pageable);
}
