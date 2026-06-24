package de.bdr.asset.management.user;

import de.bdr.asset.management.user.department.Department;
import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    private UserCreateRequestDTO buildRequest() {
        return new UserCreateRequestDTO(
                "ivan.horvat",
                "Horvat",
                "Ivan",
                "ivan@example.com",
                "password123",
                UserRoleEnum.ADMIN,
                UserStatusEnum.ACTIVE,
                1L,
                "manager@example.com",
                null,
                "some benefit"
        );
    }

    private User buildUser() {
        Department department = new Department();
        department.setId(42L);

        User user = new User();
        user.setId(1L);
        user.setUsername("ivan.horvat");
        user.setSurname("Horvat");
        user.setName("Ivan");
        user.setEmail("ivan@example.com");
        user.setPassword("password123");
        user.setRole(UserRoleEnum.ADMIN);
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setDepartment(department);
        user.setManagerEmail("manager@example.com");
        user.setNotes(null);
        user.setBenefit("some benefit");
        return user;
    }

    // --- toEntity ---

    @Test
    void shouldMapUsernameToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getUsername()).isEqualTo("ivan.horvat");
    }

    @Test
    void shouldMapSurnameToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getSurname()).isEqualTo("Horvat");
    }

    @Test
    void shouldMapNameToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getName()).isEqualTo("Ivan");
    }

    @Test
    void shouldMapEmailToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getEmail()).isEqualTo("ivan@example.com");
    }

    @Test
    void shouldMapPasswordToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getPassword()).isEqualTo("password123");
    }

    @Test
    void shouldMapRoleToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getRole()).isEqualTo(UserRoleEnum.ADMIN);
    }

    @Test
    void shouldMapStatusToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getStatus()).isEqualTo(UserStatusEnum.ACTIVE);
    }

    @Test
    void shouldMapManagerEmailToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getManagerEmail()).isEqualTo("manager@example.com");
    }

    @Test
    void shouldMapBenefitToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getBenefit()).isEqualTo("some benefit");
    }

    @Test
    void shouldIgnoreIdWhenMappingToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getId()).isNull();
    }

    @Test
    void shouldIgnoreCreatedAtWhenMappingToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getCreatedAt()).isNull();
    }

    @Test
    void shouldIgnoreLastModifiedAtWhenMappingToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getLastModifiedAt()).isNull();
    }

    @Test
    void shouldIgnoreDepartmentWhenMappingToEntity() {
        User result = userMapper.toEntity(buildRequest());
        assertThat(result.getDepartment()).isNull();
    }

    // --- toResponse ---

    @Test
    void shouldMapIdToResponse() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void shouldMapUsernameToResponse() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.username()).isEqualTo("ivan.horvat");
    }

    @Test
    void shouldMapSurnameToResponse() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.surname()).isEqualTo("Horvat");
    }

    @Test
    void shouldMapNameToResponse() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.name()).isEqualTo("Ivan");
    }

    @Test
    void shouldMapEmailToResponse() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.email()).isEqualTo("ivan@example.com");
    }

    @Test
    void shouldMapRoleToResponse() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.role()).isEqualTo(UserRoleEnum.ADMIN);
    }

    @Test
    void shouldMapStatusToResponse() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.status()).isEqualTo(UserStatusEnum.ACTIVE);
    }

    @Test
    void shouldMapManagerEmailToResponse() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.managerEmail()).isEqualTo("manager@example.com");
    }

    @Test
    void shouldMapBenefitToResponse() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.benefit()).isEqualTo("some benefit");
    }

    @Test
    void shouldMapDepartmentIdFromNestedDepartment() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.departmentId()).isEqualTo(42L);
    }

    @Test
    void shouldSetDepartmentIdToNullWhenDepartmentIsNull() {
        User user = buildUser();
        user.setDepartment(null);

        UserResponseDTO result = userMapper.toResponse(user);
        assertThat(result.departmentId()).isNull();
    }

    @Test
    void shouldMapNotesToResponse() {
        User user = buildUser();
        user.setNotes("Some note");

        UserResponseDTO result = userMapper.toResponse(user);
        assertThat(result.notes()).isEqualTo("Some note");
    }

    @Test
    void shouldMapNullNotesToResponse() {
        UserResponseDTO result = userMapper.toResponse(buildUser());
        assertThat(result.notes()).isNull();
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        User result = userMapper.toEntity(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnNullWhenUserIsNull() {
        UserResponseDTO result = userMapper.toResponse(null);
        assertThat(result).isNull();
    }

    // --- updateEntityFromDto ---

    @Test
    void shouldUpdateEntityFieldsFromDto() {
        User user = buildUser();
        user.setNotes("original note");

        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "NewSurname",
                "NewName",
                "new@example.com",
                UserRoleEnum.MANAGER,
                UserStatusEnum.INACTIVE,
                99L,
                "newmanager@example.com",
                "updated note",
                "NEW_BENEFIT"
        );

        userMapper.updateEntityFromDto(dto, user);

        assertThat(user.getSurname()).isEqualTo("NewSurname");
        assertThat(user.getName()).isEqualTo("NewName");
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getRole()).isEqualTo(UserRoleEnum.MANAGER);
        assertThat(user.getStatus()).isEqualTo(UserStatusEnum.INACTIVE);
        assertThat(user.getManagerEmail()).isEqualTo("newmanager@example.com");
        assertThat(user.getNotes()).isEqualTo("updated note");
        assertThat(user.getBenefit()).isEqualTo("NEW_BENEFIT");
    }

    @Test
    void shouldIgnoreNullFieldsWhenUpdatingEntity() {
        User user = buildUser();
        user.setSurname("OriginalSurname");
        user.setName("OriginalName");
        user.setEmail("original@example.com");
        user.setNotes("original note");

        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                null, null, null, null, null, null, null, null, null
        );

        userMapper.updateEntityFromDto(dto, user);

        assertThat(user.getSurname()).isEqualTo("OriginalSurname");
        assertThat(user.getName()).isEqualTo("OriginalName");
        assertThat(user.getEmail()).isEqualTo("original@example.com");
        assertThat(user.getNotes()).isEqualTo("original note");
    }
}