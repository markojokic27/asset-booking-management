package de.bdr.asset.management.user.department;

import de.bdr.asset.management.user.department.dtos.DepartmentRequestDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentResponseDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
import lombok.extern.slf4j.Slf4j;

/** Implementation of Department Service */
@Slf4j
@Service
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;
    private final UserRepository userRepository;
        
    public DepartmentServiceImpl(DepartmentRepository repository, DepartmentMapper mapper, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequest) {

        if (repository.existsByName(departmentRequest.name())) {
            throw new DuplicateResourceException("Department " + departmentRequest.name() + " already exists.");
        }
        
        User manager = null;
        if (departmentRequest.managerId() != null) {
            manager = userRepository.findById(departmentRequest.managerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + departmentRequest.managerId()));

            if (repository.existsByManagerId(departmentRequest.managerId())) {
                throw new DuplicateResourceException("Manager with ID " + departmentRequest.managerId() + " is already managing another department.");
            }
        }

        log.info("Manager found. Mapping entity and saving to database...");

        Department department = mapper.toEntity(departmentRequest);
        department.setManager(manager);
        department = repository.save(department);

        if (manager == null) {
            log.info("Successfully created new department with id: {} with no manager id.", department.getId());    
        } else {
            log.info("Successfully created new department with id: {} with manager id: {}", department.getId(), manager.getId());
        }

        return mapper.toResponse(department);
    }

    /** {@inheritDoc} */
    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {

        Department department = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        return mapper.toResponse(department);
    }

    /** {@inheritDoc} */
    @Override
    public Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable) {

        Page<Department> departments = repository.findAll(pageable);

        return departments.map(mapper::toResponse);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentUpdateRequestDTO departmentRequest) {

        Department department = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (departmentRequest.name() != null) {
            if (repository.existsByNameAndIdNot(departmentRequest.name(), id)) {
                throw new DuplicateResourceException("Department " + departmentRequest.name() + " already exists.");
            }
        }

        if (departmentRequest.managerId() != null) {
            User manager = userRepository.findById(departmentRequest.managerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + departmentRequest.managerId()));

            // Prevent assigning a manager who already leads another department
            if (repository.existsByManagerIdAndIdNot(departmentRequest.managerId(), id)) {
                throw new DuplicateResourceException("Manager with ID " + departmentRequest.managerId() + " is already managing another department.");
            }

            department.setManager(manager);
        }

        mapper.updateEntityFromDto(departmentRequest, department);

        department = repository.save(department);

        if (department.getManager() == null) {
            log.info("Successfully updated department with id: {} with no manager assigned.", department.getId());
        } else {
            log.info("Successfully updated department with id: {} with manager id: {}", department.getId(), department.getManager().getId());
        }

        return mapper.toResponse(department);
    }
}
