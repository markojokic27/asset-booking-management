package de.bdr.asset.management.booking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import de.bdr.asset.management.core.security.userdetails.CustomUserDetails;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
class BookingAuthorizationEvaluatorTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BookingAuthorizationEvaluator evaluator;

    private User employeeWithManager(String managerEmail) {
        var user = User.builder()
                .username("employee")
                .name("Employee")
                .surname("User")
                .email("employee@company.com")
                .password("pwd")
                .role(UserRoleEnum.EMPLOYEE)
                .status(UserStatusEnum.ACTIVE)
                .department(null)
                .managerEmail(managerEmail)
                .benefit("DESK")
                .build();
        user.setId(10L);
        return user;
    }
    private CustomUserDetails loggedInUser(String email) {
                var user = User.builder()
                        .username("manager")
                        .name("Manager")
                        .surname("User")
                        .email(email)
                        .password("pwd")
                        .role(UserRoleEnum.MANAGER)
                        .status(UserStatusEnum.ACTIVE)
                        .department(null)
                        .managerEmail("boss@company.com")
                        .benefit("ALL")
                        .build();
                user.setId(2L);
                return new CustomUserDetails(user);
    }

    // ──────────────────────────────────────────────
    // Admin bypass
    // ──────────────────────────────────────────────
    @Test
    void adminCanManageAnyBooking() {
       doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
               .when(authentication).getAuthorities();
        boolean result = evaluator.canManageBooking(authentication, 1L);
        assertThat(result).isTrue();
        verifyNoInteractions(bookingRepository);
    }
    // ──────────────────────────────────────────────
    // Manager matches employee's managerEmail
    // ──────────────────────────────────────────────
    @Test
    void managerCanManageBookingWhenEmailMatches() {
        var employee = employeeWithManager("manager@company.com");
        var booking = Booking.builder().user(employee).build();
        var principal = loggedInUser("manager@company.com");
        when(authentication.getAuthorities()).thenReturn(List.of());
        when(authentication.getPrincipal()).thenReturn(principal);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        boolean result = evaluator.canManageBooking(authentication, 1L);
        assertThat(result).isTrue();
    }
    @Test
    void managerCanManageBookingWhenEmailMatchesCaseInsensitive() {
        var employee = employeeWithManager("Manager@Company.COM");
        var booking = Booking.builder().user(employee).build();
        var principal = loggedInUser("manager@company.com");
        when(authentication.getAuthorities()).thenReturn(List.of());
        when(authentication.getPrincipal()).thenReturn(principal);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        boolean result = evaluator.canManageBooking(authentication, 1L);
        assertThat(result).isTrue();
    }
    // ──────────────────────────────────────────────
    // Manager does NOT match
    // ──────────────────────────────────────────────
    @Test
    void nonManagerCannotManageBooking() {
        var employee = employeeWithManager("real.manager@company.com");
        var booking = Booking.builder().user(employee).build();
        var principal = loggedInUser("some.other.guy@company.com");
        when(authentication.getAuthorities()).thenReturn(List.of());
        when(authentication.getPrincipal()).thenReturn(principal);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        boolean result = evaluator.canManageBooking(authentication, 1L);
        assertThat(result).isFalse();
    }
    // ──────────────────────────────────────────────
    // Booking not found
    // ──────────────────────────────────────────────
    @Test
    void throwsWhenBookingNotFound() {
        when(authentication.getAuthorities()).thenReturn(List.of());
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> evaluator.canManageBooking(authentication, 99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Booking not found");
    }

    // ──────────────────────────────────────────────
    // canCreateBooking
    // ──────────────────────────────────────────────

    @Test
    void anyoneCanCreateBookingWhenTargetUserIdIsNull() {
        boolean result = evaluator.canCreateBooking(authentication, null);
        assertThat(result).isTrue();
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void adminCanCreateBookingForAnyUser() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .when(authentication).getAuthorities();
        boolean result = evaluator.canCreateBooking(authentication, 99L);
        assertThat(result).isTrue();
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void userCanCreateBookingForSelf() {
        var principal = loggedInUser("employee@company.com");
        when(authentication.getAuthorities()).thenReturn(List.of());
        when(authentication.getPrincipal()).thenReturn(principal);
        boolean result = evaluator.canCreateBooking(authentication, 2L);
        assertThat(result).isTrue();
    }

    @Test
    void userCannotCreateBookingForOthers() {
        var principal = loggedInUser("employee@company.com");
        when(authentication.getAuthorities()).thenReturn(List.of());
        when(authentication.getPrincipal()).thenReturn(principal);
        boolean result = evaluator.canCreateBooking(authentication, 99L);
        assertThat(result).isFalse();
    }

    // ──────────────────────────────────────────────
    // canUpdateBooking
    // ──────────────────────────────────────────────

    private CustomUserDetails employeeUserDetails() {
        var user = User.builder()
                .username("emp")
                .name("Emp")
                .surname("Loyee")
                .email("emp@company.com")
                .password("pwd")
                .role(UserRoleEnum.EMPLOYEE)
                .status(UserStatusEnum.ACTIVE)
                .department(null)
                .managerEmail("manager@company.com")
                .benefit("DESK")
                .build();
        user.setId(10L);
        return new CustomUserDetails(user);
    }

    @Test
    void adminCanUpdateAnyBooking() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .when(authentication).getAuthorities();
        boolean result = evaluator.canUpdateBooking(authentication, 1L);
        assertThat(result).isTrue();
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void ownerCanUpdateOwnBooking() {
        var employee = employeeWithManager("manager@company.com");
        var booking = Booking.builder().user(employee).build();
        var principal = employeeUserDetails();
        when(authentication.getAuthorities()).thenReturn(List.of());
        when(authentication.getPrincipal()).thenReturn(principal);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        boolean result = evaluator.canUpdateBooking(authentication, 1L);
        assertThat(result).isTrue();
    }

    @Test
    void nonOwnerCannotUpdateBooking() {
        var employee = employeeWithManager("manager@company.com");
        var booking = Booking.builder().user(employee).build();
        booking.getUser().setId(99L);
        var principal = employeeUserDetails();
        when(authentication.getAuthorities()).thenReturn(List.of());
        when(authentication.getPrincipal()).thenReturn(principal);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        boolean result = evaluator.canUpdateBooking(authentication, 1L);
        assertThat(result).isFalse();
    }
}