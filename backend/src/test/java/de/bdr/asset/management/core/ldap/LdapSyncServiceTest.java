package de.bdr.asset.management.core.ldap;

import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;
import de.bdr.asset.management.user.department.Department;
import de.bdr.asset.management.user.department.DepartmentEnum;
import de.bdr.asset.management.user.department.DepartmentRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class LdapSyncServiceTest {

    @Mock
    private LdapService ldapService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LdapSyncService service;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private Department defaultDepartment;

    @BeforeEach
    void setUp() {

        defaultDepartment = new Department();
        defaultDepartment.setId(1L);
        defaultDepartment.setName(DepartmentEnum.DEVOPS);
    }

    @Test
    void shouldCreateNewUser_WhenUserDoesNotExistInDb() {

        LdapUserDTO ldapUser = new LdapUserDTO(
                "jdoe",
                "John",
                "Doe",
                "john.doe@example.com",
                "pass",
                "DEVOPS",
                null,
                "MANAGER",
                "Developer"
        );

        when(ldapService.fetchAllUsers()).thenReturn(List.of(ldapUser));
        when(userRepository.findByUsernameIn(anyList())).thenReturn(Collections.emptyList());
        when(departmentRepository.findAll()).thenReturn(List.of(defaultDepartment));
        when(passwordEncoder.encode("pass")).thenReturn("encoded_pass");

        User savedUser = new User();
        savedUser.setUsername("jdoe");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        service.syncUsers();

        verify(userRepository, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getUsername()).isEqualTo("jdoe");
        assertThat(capturedUser.getName()).isEqualTo("John");
        assertThat(capturedUser.getPassword()).isEqualTo("encoded_pass");
        assertThat(capturedUser.getRole()).isEqualTo(UserRoleEnum.MANAGER);
        assertThat(capturedUser.getStatus()).isEqualTo(UserStatusEnum.ACTIVE);
        assertThat(capturedUser.getDepartment()).isEqualTo(defaultDepartment);
        assertThat(capturedUser.getNotes()).isEqualTo("Developer");
        assertThat(capturedUser.getBenefit()).isEqualTo("STANDARD");
    }

    @Test
    void shouldCreateNewUser_WithFallbackValues_WhenFieldsAreNullOrInvalid() {

        LdapUserDTO ldapUser = new LdapUserDTO(
                "jdoe",
                "John",
                "Doe",
                "john.doe@example.com",
                null,
                "INVALID_DEPT",
                null,
                "INVALID_ROLE",
                null
        );

        when(ldapService.fetchAllUsers()).thenReturn(List.of(ldapUser));
        when(userRepository.findByUsernameIn(anyList())).thenReturn(Collections.emptyList());
        when(departmentRepository.findAll()).thenReturn(List.of(defaultDepartment));

        User savedUser = new User();
        savedUser.setUsername("jdoe");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        service.syncUsers();

        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getPassword()).isNull();
        assertThat(capturedUser.getRole()).isEqualTo(UserRoleEnum.EMPLOYEE);
        assertThat(capturedUser.getDepartment()).isEqualTo(defaultDepartment);
        assertThat(capturedUser.getNotes()).isEqualTo("LDAP sync");
    }

    @Test
    void shouldUpdateExistingUser_WhenFieldsHaveChanged() {

        LdapUserDTO ldapUser = new LdapUserDTO(
                "jdoe",
                "John New",
                "Doe New",
                "new.doe@example.com",
                "new_pass",
                "DEVOPS",
                null,
                "MANAGER",
                "Developer"
        );

        User existingUser = new User();
        existingUser.setUsername("jdoe");
        existingUser.setName("Old Name");
        existingUser.setSurname("Old Surname");
        existingUser.setEmail("old.doe@example.com");
        existingUser.setRole(UserRoleEnum.EMPLOYEE);

        when(ldapService.fetchAllUsers()).thenReturn(List.of(ldapUser));
        when(userRepository.findByUsernameIn(List.of("jdoe"))).thenReturn(List.of(existingUser));
        when(departmentRepository.findAll()).thenReturn(List.of(defaultDepartment));
        when(passwordEncoder.encode("new_pass")).thenReturn("encoded_new_pass");

        service.syncUsers();

        verify(userRepository, times(1)).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        assertThat(updatedUser.getName()).isEqualTo("John New");
        assertThat(updatedUser.getSurname()).isEqualTo("Doe New");
        assertThat(updatedUser.getEmail()).isEqualTo("new.doe@example.com");
        assertThat(updatedUser.getRole()).isEqualTo(UserRoleEnum.MANAGER);
        assertThat(updatedUser.getPassword()).isEqualTo("encoded_new_pass");
        assertThat(updatedUser.getNotes()).isEqualTo("LDAP sync");
        assertThat(updatedUser.getBenefit()).isEqualTo("STANDARD");
    }

    @Test
    void shouldNotUpdateExistingUser_WhenNoFieldsHaveChanged() {

        LdapUserDTO ldapUser = new LdapUserDTO(
                "jdoe",
                "John",
                "Doe",
                "john.doe@example.com",
                "pass",
                "DEVOPS",
                null,
                "EMPLOYEE",
                "Developer"
        );

        User existingUser = new User();
        existingUser.setUsername("jdoe");
        existingUser.setName("John");
        existingUser.setSurname("Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setRole(UserRoleEnum.EMPLOYEE);
        existingUser.setDepartment(defaultDepartment);
        existingUser.setPassword("already_set");
        existingUser.setNotes("Some note");
        existingUser.setBenefit("STANDARD");

        when(ldapService.fetchAllUsers()).thenReturn(List.of(ldapUser));
        when(userRepository.findByUsernameIn(List.of("jdoe"))).thenReturn(List.of(existingUser));
        when(departmentRepository.findAll()).thenReturn(List.of(defaultDepartment));

        service.syncUsers();

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldResolveAndSetManagerEmail() {

        LdapUserDTO ldapManager = new LdapUserDTO(
                "mBanovic",
                "Mladen",
                "Banovic",
                "mladen.banovic@example.com",
                null,
                null,
                null,
                "MANAGER",
                null
        );

        LdapUserDTO ldapEmployee = new LdapUserDTO(
                "mPlavcic",
                "Mateo",
                "Plavcic",
                "mateo.plavcic@example.com",
                null,
                null,
                "uid=mBanovic,ou=users,dc=com",
                "EMPLOYEE",
                null
        );

        User existingEmployee = new User();
        existingEmployee.setUsername("mPlavcic");

        User existingManager = new User();
        existingManager.setUsername("mBanovic");
        existingManager.setEmail("mladen.banovic@example.com");

        when(ldapService.fetchAllUsers()).thenReturn(List.of(ldapManager, ldapEmployee));
        when(userRepository.findByUsernameIn(anyList()))
                .thenReturn(List.of(existingManager, existingEmployee));
        when(departmentRepository.findAll()).thenReturn(List.of(defaultDepartment));

        service.syncUsers();

        assertThat(existingEmployee.getManagerEmail()).isEqualTo("mladen.banovic@example.com");

        verify(userRepository, atLeastOnce()).save(existingEmployee);
    }

    @Test
    void shouldIgnoreMalformedManagerDnAndDnWithoutUid() {

        LdapUserDTO employee1 = new LdapUserDTO(
                "emp1",
                "Emp",
                "One",
                "emp1@example.com",
                null,
                null,
                "invalid,,dn,,format",
                null,
                null
        );

        LdapUserDTO employee2 = new LdapUserDTO(
                "emp2",
                "Emp",
                "Two",
                "emp2@example.com",
                null,
                null,
                "cn=admin,ou=users,dc=com",
                null,
                null
        );

        User existing1 = new User(); existing1.setUsername("emp1");
        User existing2 = new User(); existing2.setUsername("emp2");

        when(ldapService.fetchAllUsers()).thenReturn(List.of(employee1, employee2));
        when(userRepository.findByUsernameIn(List.of("emp1", "emp2")))
                .thenReturn(List.of(existing1, existing2));
        when(departmentRepository.findAll()).thenReturn(List.of(defaultDepartment));

        service.syncUsers();

        assertThat(existing1.getManagerEmail()).isNull();
        assertThat(existing2.getManagerEmail()).isNull();
    }

    @Test
    void shouldThrowExceptionWhenNoDepartmentAvailableForFallback() {

        LdapUserDTO ldapUser = new LdapUserDTO(
                "jdoe",
                "John",
                "Doe",
                "john.doe@example.com",
                null,
                "INVALID_DEPT",
                null,
                null,
                null
        );

        when(ldapService.fetchAllUsers()).thenReturn(List.of(ldapUser));
        when(userRepository.findByUsernameIn(anyList())).thenReturn(Collections.emptyList());

        when(departmentRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> service.syncUsers());
    }

    @Test
    void shouldNotDoAnythingIfLdapReturnsEmptyList() {

        when(ldapService.fetchAllUsers()).thenReturn(Collections.emptyList());

        when(userRepository.findByUsernameIn(Collections.emptyList())).thenReturn(Collections.emptyList());

        service.syncUsers();

        verify(userRepository, never()).save(any());
    }
}
