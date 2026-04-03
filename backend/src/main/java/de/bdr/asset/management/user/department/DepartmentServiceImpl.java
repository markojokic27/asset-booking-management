package de.bdr.asset.management.user.department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequest) {
        log.info("Attempting to create a new department with manager id: {}", departmentRequest.managerId());

        User manager = userRepository.findById(departmentRequest.managerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + departmentRequest.managerId()));

        log.debug("Manager found. Mapping entity and saving to database...");

        Department department = mapper.toEntity(departmentRequest);
        department.setManager(manager);
        department = repository.save(department);

        log.info("Successfully created new department with id: {} with manager id: {}", department.getId(), manager.getId());

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

        log.info("Department found with id: {}", id);

        return mapper.toResponse(department);
    }

    /**
     * @return a List of DepartmentRequestDTO records
     */
    @Override
    public Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable) {
        log.debug("Fetching all departments from the database");

        Page<Department> departments = repository.findAll(pageable);

        log.info("Successfully fetched {} departments", departments.getNumberOfElements());

        return departments.map(mapper::toResponse);
    }

    /**
     * @param id - a Long id
     * @param departmentRequest - a DepartmentRequestDTO record
     * @return a DepartmentRequestDTO record
     */
    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO departmentRequest) {
        log.info("Attempting to update department with id: {}", id);

        Department department = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        User manager = userRepository.findById(departmentRequest.managerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + departmentRequest.managerId()));
        
        department.setName(departmentRequest.name());
        department.setManager(manager);
        department = repository.save(department);

        log.info("Successfully updated department with id: {}", id);

        return mapper.toResponse(department);
    }

    /**
     * @param id - a Long id
     */
    @Override
    public void deleteDepartment(Long id) {
        // TODO: Add a field for soft delete
        
        // Department department = repository.findById(id)
        //         .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        // department.setStatus("DELETED");
        // department = repository.save();
    }
}
