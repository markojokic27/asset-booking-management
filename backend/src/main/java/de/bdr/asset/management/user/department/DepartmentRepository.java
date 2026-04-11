package de.bdr.asset.management.user.department;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Department Repository
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByName(DepartmentEnum name);

    boolean existsByNameAndIdNot(DepartmentEnum name, Long id);

    boolean existsByManagerId(Long managerId);

    boolean existsByManagerIdAndIdNot(Long managerId, Long departmentId);
}
