package de.bdr.asset.management.department;

import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.department.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DepartmentMapperTest {

    private DepartmentMapper departmentMapper;

    @BeforeEach
    void setUp() {
        departmentMapper = new DepartmentMapperImpl();
    }

    private DepartmentRequestDTO buildRequest() {
        return new DepartmentRequestDTO(
                DepartmentEnum.DEVOPS,
                1L
        );
    }

    private Department buildDepartment() {
        User manager = new User();
        manager.setId(1L);

        Department department = new Department();
        department.setId(10L);
        department.setName(DepartmentEnum.DEVOPS);
        department.setManager(manager);
        return department;
    }

    // --- toEntity ---

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        Department result = departmentMapper.toEntity(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldMapNameToEntity() {
        Department result = departmentMapper.toEntity(buildRequest());
        assertThat(result.getName()).isEqualTo(DepartmentEnum.DEVOPS);
    }

    @Test
    void shouldIgnoreIdWhenMappingToEntity() {
        Department result = departmentMapper.toEntity(buildRequest());
        assertThat(result.getId()).isNull();
    }

    @Test
    void shouldIgnoreManagerWhenMappingToEntity() {
        Department result = departmentMapper.toEntity(buildRequest());
        assertThat(result.getManager()).isNull();
    }

    @Test
    void shouldIgnoreCreatedAtWhenMappingToEntity() {
        Department result = departmentMapper.toEntity(buildRequest());
        assertThat(result.getCreatedAt()).isNull();
    }

    @Test
    void shouldIgnoreLastModifiedAtWhenMappingToEntity() {
        Department result = departmentMapper.toEntity(buildRequest());
        assertThat(result.getLastModifiedAt()).isNull();
    }

    // --- toResponse ---

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        DepartmentResponseDTO result = departmentMapper.toResponse(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldMapIdToResponse() {
        DepartmentResponseDTO result = departmentMapper.toResponse(buildDepartment());
        assertThat(result.id()).isEqualTo(10L);
    }

    @Test
    void shouldMapNameToResponse() {
        DepartmentResponseDTO result = departmentMapper.toResponse(buildDepartment());
        assertThat(result.name()).isEqualTo(DepartmentEnum.DEVOPS);
    }

    @Test
    void shouldMapManagerIdFromNestedManager() {
        DepartmentResponseDTO result = departmentMapper.toResponse(buildDepartment());
        assertThat(result.managerId()).isEqualTo(1L);
    }

    @Test
    void shouldSetManagerIdToNullWhenManagerIsNull() {
        Department department = buildDepartment();
        department.setManager(null);

        DepartmentResponseDTO result = departmentMapper.toResponse(department);
        assertThat(result.managerId()).isNull();
    }

    @Test
    void shouldMapNullManagerIdRequestToEntity() {
        DepartmentRequestDTO request = new DepartmentRequestDTO(DepartmentEnum.DEVOPS, null);

        Department result = departmentMapper.toEntity(request);
        assertThat(result.getManager()).isNull();
    }
}