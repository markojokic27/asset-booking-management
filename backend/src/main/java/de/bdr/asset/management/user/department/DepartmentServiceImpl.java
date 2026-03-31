package de.bdr.asset.management.user.department;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
/**
 * Implementation of Department Service
 * Currently returns only dummy data.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    // TODO: Update the functions to not use dummy data
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
        User manager = userRepository.findById(departmentRequest.managerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + departmentRequest.managerId()));

        Department department = mapper.toEntity(departmentRequest);
        department.setManager(manager);
        department = repository.save(department);

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
     * @return a List of DepartmentRequestDTO records
     */
    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        List<Department> departments = repository.findAll();

        return departments.stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * @param id - a Long id
     * @param departmentRequest - a DepartmentRequestDTO record
     * @return a DepartmentRequestDTO record
     */
    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO departmentRequest) {
        Department department = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        User manager = userRepository.findById(departmentRequest.managerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + departmentRequest.managerId()));
        
        department.setName(departmentRequest.name());
        department.setManager(manager);
        department = repository.save(department);

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
