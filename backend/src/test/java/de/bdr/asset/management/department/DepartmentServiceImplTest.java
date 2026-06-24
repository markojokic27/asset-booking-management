package de.bdr.asset.management.department;

import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;

import de.bdr.asset.management.user.department.*;
import de.bdr.asset.management.user.department.dtos.DepartmentRequestDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentResponseDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentUpdateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository repository;

    @Mock
    private DepartmentMapper mapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DepartmentServiceImpl service;

    private Department department;
    private User manager;
    private DepartmentRequestDTO requestDTO;
    private DepartmentUpdateRequestDTO updateRequestDTO;
    private DepartmentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        manager = new User();
        manager.setId(1L);
        manager.setName("ivan ivic");

        department = new Department();
        department.setId(1L);
        department.setName(DepartmentEnum.DEVOPS);
        department.setManager(manager);

        requestDTO = new DepartmentRequestDTO(
                DepartmentEnum.DEVOPS,
                1L
        );

        updateRequestDTO = new DepartmentUpdateRequestDTO(
                DepartmentEnum.DEVOPS,
                1L
        );

        responseDTO = new DepartmentResponseDTO(
                1L,
                DepartmentEnum.DEVOPS,
                1L
        );
    }

    // Tests createDepartment(): manager exists, department saved
    @Test
    void shouldCreateDepartment() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(mapper.toEntity(requestDTO)).thenReturn(department);
        when(repository.save(department)).thenReturn(department);
        when(mapper.toResponse(department)).thenReturn(responseDTO);

        DepartmentResponseDTO result = service.createDepartment(requestDTO);

        assertNotNull(result);
        assertEquals(DepartmentEnum.DEVOPS, result.name());
        verify(repository).save(department);
        verify(mapper).toResponse(department);
    }

    // Tests createDepartment(): throws if manager not found
    @Test
    void shouldThrowExceptionWhenManagerNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createDepartment(requestDTO));

        verify(repository, never()).save(any());
    }

    // Tests getDepartmentById(): department found
    @Test
    void shouldGetDepartmentById() {
        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(mapper.toResponse(department)).thenReturn(responseDTO);

        DepartmentResponseDTO result = service.getDepartmentById(1L);

        assertEquals(1L, result.id());
        verify(repository).findById(1L);
    }

    // Tests getDepartmentById(): throws if not found
    @Test
    void shouldThrowExceptionWhenDepartmentNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getDepartmentById(1L));
    }

    // Tests getAllDepartments(): fetch all departments
    @Test
    void shouldReturnAllDepartments() {
        Page<Department> page = new PageImpl<>(List.of(department));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(department)).thenReturn(responseDTO);

        Page<DepartmentResponseDTO> result = service.getAllDepartments(pageable);

        assertEquals(1, result.getNumberOfElements());
        verify(repository).findAll(pageable);
    }

    // Tests updateDepartment(): department exists, manager exists, update saved
    @Test
    void shouldUpdateDepartment() {
        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(repository.existsByNameAndIdNot(DepartmentEnum.DEVOPS, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(repository.existsByManagerIdAndIdNot(1L, 1L)).thenReturn(false);
        when(repository.save(department)).thenReturn(department);
        when(mapper.toResponse(department)).thenReturn(responseDTO);

        DepartmentResponseDTO result = service.updateDepartment(1L, updateRequestDTO);

        assertEquals(DepartmentEnum.DEVOPS, result.name());
        verify(mapper).updateEntityFromDto(updateRequestDTO, department);
        verify(repository).save(department);
    }

    // Tests updateDepartment(): throws if department not found
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingDepartment() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateDepartment(1L, updateRequestDTO));
    }

    // Tests updateDepartment(): throws if manager not found
    @Test
    void shouldThrowExceptionWhenUpdatingDepartmentWithNonExistingManager() {
        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(repository.existsByNameAndIdNot(DepartmentEnum.DEVOPS, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateDepartment(1L, updateRequestDTO));
    }

    // Tests updateDepartment(): throws if department name already exists elsewhere
    @Test
    void shouldThrowExceptionWhenUpdatingDepartmentWithDuplicateName() {
        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(repository.existsByNameAndIdNot(DepartmentEnum.DEVOPS, 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.updateDepartment(1L, updateRequestDTO));

        verify(repository, never()).save(any());
    }

    // Tests updateDepartment(): throws if manager already manages another department
    @Test
    void shouldThrowExceptionWhenManagerAlreadyAssignedToAnotherDepartment() {
        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(repository.existsByNameAndIdNot(DepartmentEnum.DEVOPS, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(repository.existsByManagerIdAndIdNot(1L, 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.updateDepartment(1L, updateRequestDTO));

        verify(repository, never()).save(any());
    }

    // Tests createDepartment(): throws if name already exists
    @Test
    void shouldThrowExceptionWhenCreatingDepartmentWithDuplicateName() {
        when(repository.existsByName(requestDTO.name())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.createDepartment(requestDTO));

        verify(repository).existsByName(requestDTO.name());
        verify(repository, never()).save(any());
    }

    // Tests createDepartment(): throws if manager already assigned to another department
    @Test
    void shouldThrowExceptionWhenCreatingDepartmentWithManagerAlreadyAssigned() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(repository.existsByName(requestDTO.name())).thenReturn(false);
        when(repository.existsByManagerId(1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.createDepartment(requestDTO));

        verify(repository, never()).save(any());
    }

    // Tests createDepartment(): succeeds with null managerId (manager is optional)
    @Test
    void shouldCreateDepartmentWithNullManagerId() {
        DepartmentRequestDTO requestWithoutManager = new DepartmentRequestDTO(DepartmentEnum.DEVOPS, null);

        when(repository.existsByName(requestWithoutManager.name())).thenReturn(false);
        when(mapper.toEntity(requestWithoutManager)).thenReturn(department);
        when(repository.save(department)).thenReturn(department);
        when(mapper.toResponse(department)).thenReturn(responseDTO);

        DepartmentResponseDTO result = service.createDepartment(requestWithoutManager);

        assertNotNull(result);
        verify(repository).save(department);
        verify(userRepository, never()).findById(any());
    }

    // Tests updateDepartment(): skips duplicate name check when name is null
    @Test
    void shouldUpdateDepartmentWithNullName() {
        DepartmentUpdateRequestDTO updateWithoutName = new DepartmentUpdateRequestDTO(null, 1L);

        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(repository.existsByManagerIdAndIdNot(1L, 1L)).thenReturn(false);
        when(repository.save(department)).thenReturn(department);
        when(mapper.toResponse(department)).thenReturn(responseDTO);

        DepartmentResponseDTO result = service.updateDepartment(1L, updateWithoutName);

        assertEquals(DepartmentEnum.DEVOPS, result.name());
        verify(mapper).updateEntityFromDto(updateWithoutName, department);
        verify(repository).save(department);
        verify(repository, never()).existsByNameAndIdNot(any(), any());
    }

    // Tests updateDepartment(): skips manager lookup when managerId is null
    @Test
    void shouldUpdateDepartmentWithNullManagerId() {
        DepartmentUpdateRequestDTO updateWithoutManager = new DepartmentUpdateRequestDTO(DepartmentEnum.DEVOPS, null);

        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(repository.existsByNameAndIdNot(DepartmentEnum.DEVOPS, 1L)).thenReturn(false);
        when(repository.save(department)).thenReturn(department);
        when(mapper.toResponse(department)).thenReturn(responseDTO);

        DepartmentResponseDTO result = service.updateDepartment(1L, updateWithoutManager);

        assertEquals(DepartmentEnum.DEVOPS, result.name());
        verify(mapper).updateEntityFromDto(updateWithoutManager, department);
        verify(repository).save(department);
        verify(userRepository, never()).findById(any());
    }
}
