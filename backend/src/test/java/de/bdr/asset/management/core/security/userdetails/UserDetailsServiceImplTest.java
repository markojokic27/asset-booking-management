package de.bdr.asset.management.core.security.userdetails;

import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("ivan.horvat");
        user.setPassword("password123");
        user.setName("Ivan");
        user.setSurname("Horvat");
        user.setEmail("ivan@example.com");
        user.setRole(UserRoleEnum.EMPLOYEE);
        user.setStatus(UserStatusEnum.ACTIVE);
    }

    @Test
    void shouldReturnCustomUserDetailsWhenUserExists() {
        when(userRepository.findByUsername("ivan.horvat")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("ivan.horvat");

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CustomUserDetails.class);
    }

    @Test
    void shouldReturnCorrectUsernameWhenUserExists() {
        when(userRepository.findByUsername("ivan.horvat")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("ivan.horvat");

        assertThat(result.getUsername()).isEqualTo("ivan.horvat");
    }

    @Test
    void shouldReturnCorrectPasswordWhenUserExists() {
        when(userRepository.findByUsername("ivan.horvat")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("ivan.horvat");

        assertThat(result.getPassword()).isEqualTo("password123");
    }

    @Test
    void shouldReturnCorrectRoleWhenUserExists() {
        when(userRepository.findByUsername("ivan.horvat")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("ivan.horvat");

        assertThat(result.getAuthorities())
                .anyMatch(a -> "ROLE_EMPLOYEE".equals(a.getAuthority()));
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown.user"));
    }

    @Test
    void shouldCallRepositoryWithCorrectUsername() {
        when(userRepository.findByUsername("ivan.horvat")).thenReturn(Optional.of(user));

        userDetailsService.loadUserByUsername("ivan.horvat");

        verify(userRepository).findByUsername("ivan.horvat");
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserIsDeleted() {
        user.setStatus(UserStatusEnum.DELETED);
        when(userRepository.findByUsername("ivan.horvat")).thenReturn(Optional.of(user));

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("ivan.horvat"));
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserIsInactive() {
        user.setStatus(UserStatusEnum.INACTIVE);
        when(userRepository.findByUsername("ivan.horvat")).thenReturn(Optional.of(user));

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("ivan.horvat"));
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserLeftCompany() {
        user.setStatus(UserStatusEnum.LEFT_COMPANY);
        when(userRepository.findByUsername("ivan.horvat")).thenReturn(Optional.of(user));

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("ivan.horvat"));
    }

    @Test
    void shouldReturnCustomUserDetailsWhenUserIsStudent() {
        user.setStatus(UserStatusEnum.STUDENT);
        when(userRepository.findByUsername("ivan.horvat")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("ivan.horvat");

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CustomUserDetails.class);
    }
}