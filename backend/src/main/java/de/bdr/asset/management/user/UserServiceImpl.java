package de.bdr.asset.management.user;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.department.Department;
import de.bdr.asset.management.user.department.DepartmentRepository;
import de.bdr.asset.management.user.dtos.ChangePasswordRequestDTO;
import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** Implementation of User Service */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    /** Error message blueprint when a requested gituser identity is missing. */
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with id: ";

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO createUser(UserCreateRequestDTO userRequest) {

        if (userRepository.existsByUsername(userRequest.username())) {
            throw new DuplicateResourceException("Username " + userRequest.username() + " is already taken");
        }

        if (userRepository.existsByEmail(userRequest.email())) {
            throw new DuplicateResourceException("Email " + userRequest.email() + " is already in use");
        }

        Department department = departmentRepository.findById(userRequest.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + userRequest.departmentId()));

        User user = mapper.toEntity(userRequest);
        user.setDepartment(department);
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        User savedUser = userRepository.save(user);

        return mapper.toResponse(savedUser);
    }

    /** {@inheritDoc} */
    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_WITH_ID + id));

        return mapper.toResponse(user);
    }

    /** {@inheritDoc} */
    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {

        Page<User> users = userRepository.findAll(pageable);

        return users.map(mapper::toResponse);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO userUpdateRequest) {

        User user = userRepository.findById(id)
                .filter(u -> u.getStatus() != UserStatusEnum.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_WITH_ID + id));

        mapper.updateEntityFromDto(userUpdateRequest, user);

        if (userUpdateRequest.departmentId() != null) {
            Department department = departmentRepository.findById(userUpdateRequest.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + userUpdateRequest.departmentId()));

            user.setDepartment(department);
        }

        userRepository.save(user);
        return mapper.toResponse(user);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRequestDTO request) {

        User user = userRepository.findById(id)
                .filter(u -> u.getStatus() != UserStatusEnum.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_WITH_ID + id));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password does not match");
        }

        String newEncodedPassword = passwordEncoder.encode(request.newPassword());
        user.setPassword(newEncodedPassword);
        userRepository.save(user);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_WITH_ID + id));

        user.setStatus(UserStatusEnum.DELETED);
        userRepository.save(user);

        eventPublisher.publishEvent(new UserSoftDeletedEvent(this, id));
    }

    @Override
    public User getActiveOrStudentUserById(Long id) {
        return userRepository.findByIdAndStatusIn(
                id,
                List.of(UserStatusEnum.ACTIVE, UserStatusEnum.STUDENT)
        ).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_WITH_ID + id));
    }
}
