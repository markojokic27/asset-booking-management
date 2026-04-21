package de.bdr.asset.management.user;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.asset.AssetStatusEnum;
import de.bdr.asset.management.booking.BookingRepository;
import de.bdr.asset.management.booking.BookingStatusEnum;
import de.bdr.asset.management.core.exception.DuplicateResourceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.department.Department;
import de.bdr.asset.management.user.department.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of User Service
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    // TODO: Add documentation like other service implementations

    private final UserRepository repository;
    private final UserMapper mapper;
    private final DepartmentRepository departmentRepository;
    private final BookingRepository bookingRepository;

    public UserServiceImpl(UserRepository repository, UserMapper mapper, DepartmentRepository departmentRepository, BookingRepository bookingRepository) {

        this.repository = repository;
        this.mapper = mapper;
        this.departmentRepository = departmentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO createUser(UserRequestDTO userRequest) {

        log.info("Attempting to create a new user for department id: {}", userRequest.departmentId());

        if (repository.existsByUsername(userRequest.username())) {
            throw new DuplicateResourceException("Username " + userRequest.username() + " is already taken");
        }

        if (repository.existsByEmail(userRequest.email())) {
            throw new DuplicateResourceException("Email " + userRequest.email() + " is already in use");
        }

        Department department = departmentRepository.findById(userRequest.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + userRequest.departmentId()));

        log.debug("Department found. Mapping entity and saving to database...");
        
        User user = mapper.toEntity(userRequest);
        user.setDepartment(department);
        user = repository.save(user);

        log.info("Successfully created new user with id: {} in department id: {}", user.getId(), department.getId());

        return mapper.toResponse(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        log.info("User found with id: {}", id);

        return mapper.toResponse(user);
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {

        log.debug("Fetching users from the database with pagination: " +
                        "Page number: {} | Page size: {} | Sort: {}",
                        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()
        );

        Page<User> users = repository.findAll(pageable);

        log.info("Successfully fetched {} users", users.getNumberOfElements());

        return users.map(mapper::toResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequest) {

        log.info("Attempting to update user with id: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (repository.existsByUsernameAndIdNot(userRequest.username(), id)) {
            throw new DuplicateResourceException("Username " + userRequest.username() + " is already taken by another user.");
        }

        if (repository.existsByEmailAndIdNot(userRequest.email(), id)) {
            throw new DuplicateResourceException("Email " + userRequest.email() + " is already in use by another user.");
        }

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

        log.info("Successfully updated user with id: {}", id);

        return mapper.toResponse(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteUser(Long id) {

        log.info("Attempting to delete user with id: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setStatus(UserStatusEnum.DELETED);

        repository.save(user);

        List<String> statusesToCancel = List.of(
                BookingStatusEnum.ACTIVE.name(),
                BookingStatusEnum.APPROVED.name(),
                BookingStatusEnum.PENDING.name()
        );

        bookingRepository.cancelNotFinishedBookingsForUser(id, statusesToCancel);
    }
}
