package de.bdr.asset.management.user;

import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.user.department.Department;
import de.bdr.asset.management.user.department.DepartmentEnum;
import de.bdr.asset.management.user.department.DepartmentRepository;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;

import de.bdr.asset.management.user.dtos.ChangePasswordRequestDTO;
import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    private User user;
    private Department department;
    private UserCreateRequestDTO requestDTO;
    private UserResponseDTO responseDTO;
    private UserUpdateRequestDTO userUpdateRequestDTO;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName(DepartmentEnum.DEVOPS);

        user = new User();
        user.setId(1L);
        user.setName("ivan ivic");
        user.setDepartment(department);

        requestDTO = new UserCreateRequestDTO(
                "ivanivic",
                "ivic",
                "ivan",
                "iivanivic@maurer-electonics.hr",
                "password.123",
                UserRoleEnum.EMPLOYEE,
                UserStatusEnum.ACTIVE,
                1L,
                "antem@maurer-electonics.hr",
                "Some optional notes",
                "ALL"
        );

        userUpdateRequestDTO = new UserUpdateRequestDTO(
                "ivic",
                "ivan",
                "iivanivic@maurer-electonics.hr",
                UserRoleEnum.EMPLOYEE,
                UserStatusEnum.ACTIVE,
                1L,
                "antem@maurer-electonics.hr",
                "Some updated notes",
                "SOME"
        );

        responseDTO = new UserResponseDTO(
                1L,
                "ivanivic",
                "ivic",
                "ivan",
                "iivanivic@maurer-electonics.hr",
                UserRoleEnum.EMPLOYEE,
                UserStatusEnum.ACTIVE,
                1L,
                "antem@maurer-electonics.hr",
                "Some optional notes",
                "ALL"
        );
    }

    // Tests createUser(): department exists, user saved
    @Test
    void shouldCreateUser() {

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(mapper.toEntity(requestDTO)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = service.createUser(requestDTO);

        assertNotNull(result);
        assertEquals("ivan", result.name());
        verify(repository).save(user);
        verify(mapper).toResponse(user);
    }

    // Tests createUser(): throws if department not found
    @Test
    void shouldThrowExceptionWhenDepartmentNotFound() {

        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createUser(requestDTO));

        verify(repository, never()).save(any());
    }

    // Tests createUser(): throws exception if username already exists
    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        when(repository.existsByUsername(requestDTO.username())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.createUser(requestDTO));


        verify(repository).existsByUsername(requestDTO.username());
        verify(repository, never()).existsByEmail(any());
        verify(repository, never()).save(any());
    }

    // Tests createUser(): throws exception if email already exists
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(repository.existsByUsername(requestDTO.username())).thenReturn(false);
        when(repository.existsByEmail(requestDTO.email())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.createUser(requestDTO));

        verify(repository).existsByUsername(requestDTO.username());
        verify(repository).existsByEmail(requestDTO.email());
        verify(repository, never()).save(any());
    }

    // Tests getUserById(): user found
    @Test
    void shouldGetUserById() {

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = service.getUserById(1L);

        assertEquals(1L, result.id());
        verify(repository).findById(1L);
    }

    // Tests getUserById(): throws if not found
    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getUserById(1L));
    }

    // Tests getAllUsers(): fetch all users
    @Test
    void shouldReturnAllUsers() {

        Page<User> page = new PageImpl<>(List.of(user));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        Page<UserResponseDTO> result = service.getAllUsers(pageable);

        assertEquals(1, result.getNumberOfElements());
        verify(repository).findAll(pageable);
    }

    // Tests updateUser(): user exists, department exists, update saved
    @Test
    void shouldUpdateUser() {

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        Department mockDepartment = new Department();
        mockDepartment.setId(1L);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(mockDepartment));

        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = service.updateUser(1L, userUpdateRequestDTO);

        assertEquals("ivan", result.name());
        verify(repository).save(user);
        verify(departmentRepository).findById(1L);
    }

    // Tests updateUser(): throws if user not found
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateUser(1L, userUpdateRequestDTO));
    }

    // Tests softDeleteUser(): user exists → status set to DELETED, save user, cancel active bookings
    @Test
    void shouldSoftDeleteUser() {

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        service.softDeleteUser(1L);

        assertEquals(UserStatusEnum.DELETED, user.getStatus());

        verify(repository).findById(1L);
        verify(repository).save(user);
        verify(eventPublisher).publishEvent(any(UserSoftDeletedEvent.class));
    }

    // Tests softDeleteUser(): throws exception if user does not exist
    @Test
    void shouldThrowExceptionWhenSoftDeletingNonExistingUser() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.softDeleteUser(1L));

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    // Tests changePassword(): user exists, active, old password matches -> encode new, save user
    @Test
    void shouldChangePasswordSuccessfully() {

        ChangePasswordRequestDTO passwordRequest = new ChangePasswordRequestDTO("oldPass", "newPass");
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setPassword("encodedOldPass");

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        service.changePassword(1L, passwordRequest);

        assertEquals("encodedNewPass", user.getPassword());

        verify(repository).findById(1L);
        verify(passwordEncoder).matches("oldPass", "encodedOldPass");
        verify(passwordEncoder).encode("newPass");
        verify(repository).save(user);
    }

    // Tests changePassword(): throws exception if user not found
    @Test
    void shouldThrowExceptionWhenChangingPasswordForNonExistingUser() {

        ChangePasswordRequestDTO passwordRequest = new ChangePasswordRequestDTO("oldPass", "newPass");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.changePassword(1L, passwordRequest));

        verify(repository).findById(1L);
        verify(passwordEncoder, never()).matches(any(), any());
        verify(repository, never()).save(any());
    }

    // Tests changePassword(): throws exception if user is DELETED
    @Test
    void shouldThrowExceptionWhenChangingPasswordForDeletedUser() {

        ChangePasswordRequestDTO passwordRequest = new ChangePasswordRequestDTO("oldPass", "newPass");
        user.setStatus(UserStatusEnum.DELETED);

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class,
                () -> service.changePassword(1L, passwordRequest));

        verify(repository).findById(1L);
        verify(passwordEncoder, never()).matches(any(), any());
        verify(repository, never()).save(any());
    }

    // Tests changePassword(): throws exception if current password does not match
    @Test
    void shouldThrowExceptionWhenCurrentPasswordDoesNotMatch() {
        ChangePasswordRequestDTO passwordRequest = new ChangePasswordRequestDTO("wrongOldPass", "newPass");
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setPassword("encodedOldPass");

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOldPass", "encodedOldPass")).thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> service.changePassword(1L, passwordRequest));

        verify(repository).findById(1L);
        verify(passwordEncoder).matches("wrongOldPass", "encodedOldPass");
        verify(passwordEncoder, never()).encode(any());
        verify(repository, never()).save(any());
    }
}