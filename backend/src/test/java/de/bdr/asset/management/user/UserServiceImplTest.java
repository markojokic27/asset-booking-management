package de.bdr.asset.management.user;

import de.bdr.asset.management.user.department.Department;
import de.bdr.asset.management.user.department.DepartmentEnum;
import de.bdr.asset.management.user.department.DepartmentRepository;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private UserServiceImpl service;

    private User user;
    private Department department;
    private UserRequestDTO requestDTO;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName(DepartmentEnum.DEVOPS);

        user = new User();
        user.setId(1L);
        user.setName("ivan ivic");
        user.setDepartment(department);

        requestDTO = new UserRequestDTO(
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
        when(repository.findAll()).thenReturn(List.of(user));
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        List<UserResponseDTO> result = service.getAllUsers();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    // Tests updateUser(): user exists, department exists, update saved
    @Test
    void shouldUpdateUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = service.updateUser(1L, requestDTO);

        assertEquals("ivan", result.name());
        verify(repository).save(user);
    }

    // Tests updateUser(): throws if user not found
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateUser(1L, requestDTO));
    }

    // Tests updateUser(): throws if department not found
    @Test
    void shouldThrowExceptionWhenUpdatingUserWithNonExistingDepartment() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateUser(1L, requestDTO));
    }
}