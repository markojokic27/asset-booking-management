package de.bdr.asset.management.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.department.Department;
import de.bdr.asset.management.user.department.DepartmentRepository;

/**
 * Implementation of User Service
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final DepartmentRepository departmentRepository;

    public UserServiceImpl(UserRepository repository, UserMapper mapper, DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        Department department = departmentRepository.findById(userRequest.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + userRequest.departmentId()));
        
        User user = mapper.toEntity(userRequest);
        user.setDepartment(department);
        user = repository.save(user);

        return mapper.toResponse(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return mapper.toResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = repository.findAll();

        return users.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequest) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        Department department = departmentRepository.findById(userRequest.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + userRequest.departmentId()));
        
        user.setUsername(userRequest.username());
        user.setSurname(userRequest.surname());
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setRole(userRequest.role());
        user.setStatus(userRequest.status());
        user.setDepartment(department);
        user.setManagerEmail(userRequest.managerEmail());
        user.setNotes(userRequest.notes());
        user.setBenefit(userRequest.benefit());
        user = repository.save(user);

        return mapper.toResponse(user);
    }

    @Override
    public UserResponseDTO deleteUser(Long id, String status, String note) {
        // TODO: Add a field for soft delete
        // Also discuss if passing status is neccessary since delete status will always be the same
        
        // User user = repository.findById(id)
        //         .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // user.setStatus(status);
        // user.setNote(note);
        // user = repository.save();

        return null;
    }
}
