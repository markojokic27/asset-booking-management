package de.bdr.asset.management.user;

import de.bdr.asset.management.booking.BookingRepository;
import de.bdr.asset.management.booking.BookingStatusEnum;
import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.user.dtos.ChangePasswordRequestDTO;
import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final BookingRepository bookingRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO createUser(UserCreateRequestDTO userRequest) {

        log.info("Attempting to create a new user for department id: {}", userRequest.departmentId());

        if (userRepository.existsByUsername(userRequest.username())) {
            throw new DuplicateResourceException("Username " + userRequest.username() + " is already taken");
        }

        if (userRepository.existsByEmail(userRequest.email())) {
            throw new DuplicateResourceException("Email " + userRequest.email() + " is already in use");
        }

        Department department = departmentRepository.findById(userRequest.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + userRequest.departmentId()));

        log.debug("Department found. Mapping entity and saving to database...");
        
        User user = mapper.toEntity(userRequest);
        user.setDepartment(department);
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        userRepository.save(user);

        log.info("Successfully created new user with id: {} in department id: {}", user.getId(), department.getId());

        return mapper.toResponse(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
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

        Page<User> users = userRepository.findAll(pageable);

        log.info("Successfully fetched {} users", users.getNumberOfElements());

        return users.map(mapper::toResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO userUpdateRequest) {

        log.info("Attempting to update user with id: {}", id);

        // NOTE:
        //
        // we could define following method in repository to avoid filtering for deleted users:
        //
        //      Optional<User> findByIdAndStatusNot(Long id, UserStatusEnum status);
        //
        // and then pass status DELETED when calling:
        //
        //      User user = userRepository.findByIdAndStatusNot(id, UserStatusEnum.DELETED)
        //                      .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        //

        User user = userRepository.findById(id)
                .filter(u -> u.getStatus() != UserStatusEnum.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // TODO
        if (userUpdateRequest.name() != null) {
            user.setName(userUpdateRequest.name());
        }

        if (userUpdateRequest.surname() != null) {
            user.setSurname(userUpdateRequest.surname());
        }

        if (userUpdateRequest.email() != null) {
            user.setEmail(userUpdateRequest.email());
        }

        if (userUpdateRequest.role() != null) {
            user.setRole(userUpdateRequest.role());
        }

        /*
        if (userUpdateRequest.departmentId() != null) {
            user.setDepartment(userUpdateRequest.departmentId());
        }
        */

        if (userUpdateRequest.managerEmail() != null) {
            user.setManagerEmail(userUpdateRequest.managerEmail());
        }

        if (userUpdateRequest.status() != null) {
            user.setStatus(userUpdateRequest.status());
        }

        if (userUpdateRequest.notes() != null) {
            user.setNotes(userUpdateRequest.notes());
        }

        if (userUpdateRequest.benefit() != null) {
            user.setBenefit(userUpdateRequest.benefit());
        }

        userRepository.save(user);

        log.info("Successfully updated user with id: {}", id);

        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRequestDTO request) {

        log.info("Attempting to change password for user with id: {}", id);

        User user = userRepository.findById(id)
                .filter(u -> u.getStatus() != UserStatusEnum.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password does not match");
        }

        String newEncodedPassword = passwordEncoder.encode(request.newPassword());

        user.setPassword(newEncodedPassword);

        userRepository.save(user);

        log.info("Successfully changed password for user with id: {}", id);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteUser(Long id) {

        log.info("Attempting to delete user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setStatus(UserStatusEnum.DELETED);

        userRepository.save(user);

        List<String> statusesToCancel = List.of(
                BookingStatusEnum.ACTIVE.name(),
                BookingStatusEnum.APPROVED.name(),
                BookingStatusEnum.PENDING.name()
        );

        bookingRepository.cancelNotFinishedBookingsForUser(id, statusesToCancel);
    }
}
