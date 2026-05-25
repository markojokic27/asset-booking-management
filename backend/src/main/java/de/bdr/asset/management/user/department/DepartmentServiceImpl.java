package de.bdr.asset.management.user.department;

import de.bdr.asset.management.user.department.dtos.DepartmentRequestDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of Department Service
 * Currently returns only dummy data.
 */
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

    /**
     * @param departmentRequest - A DepartmentDTO record
     * @return a DepartmentDTO record
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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

    /**
     * @param id - a Long id
     * @return a DepartmentRequestDTO record
     */
    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {

        Department department = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        return mapper.toResponse(department);
    }

    /**
     * @param pageable - A Pageable object, determines the page, size and sort
     * @return a Page of DepartmentRequestDTO records
     */
    @Override
    public Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable) {

        Page<Department> departments = repository.findAll(pageable);

        return departments.map(mapper::toResponse);
    }

    /**
     * @param id - a Long id
     * @param departmentRequest - a DepartmentRequestDTO record
     * @return a DepartmentRequestDTO record
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO departmentRequest) {

        Department department = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (repository.existsByNameAndIdNot(departmentRequest.name(), id)) {
            throw new DuplicateResourceException("Department " + departmentRequest.name() + " already exists.");
        }

        User manager = null;
        if (departmentRequest.managerId() != null) {
            manager = userRepository.findById(departmentRequest.managerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + departmentRequest.managerId()));

            if (repository.existsByManagerIdAndIdNot(departmentRequest.managerId(), id)) {
                throw new DuplicateResourceException("Manager with ID " + departmentRequest.managerId() + " is already managing another department.");
            }
        }
        
        department.setName(departmentRequest.name());
        department.setManager(manager);
        department = repository.save(department);

        if (manager == null) {
            log.info("Successfully updated new department with id: {} with no manager id.", department.getId());
        } else {
            log.info("Successfully updated new department with id: {} with manager id: {}", department.getId(), manager.getId());
        }

        return mapper.toResponse(department);
    }
}
