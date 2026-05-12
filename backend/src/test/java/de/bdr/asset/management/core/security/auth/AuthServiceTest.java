package de.bdr.asset.management.core.security.auth;

import de.bdr.asset.management.core.security.jwt.JwtTokenProvider;
import de.bdr.asset.management.core.security.userdetails.CustomUserDetails;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private CustomUserDetails userDetails;

    @InjectMocks
    private AuthService authService;

    private final LoginRequestDTO loginRequest = new LoginRequestDTO("ivan.horvat", "password123");

    // --- login ---

    @Test
    void shouldReturnTokensOnSuccessfulLogin() {

        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authManager.authenticate(any())).thenReturn(authentication);
        when(tokenProvider.generateAccessToken(userDetails)).thenReturn("access-token");
        when(tokenProvider.generateRefreshToken(userDetails)).thenReturn("refresh-token");

        LoginResponseDTO result = authService.login(loginRequest);

        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void shouldCallAuthManagerOnLogin() {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(authManager.authenticate(any())).thenReturn(auth);

        authService.login(loginRequest);

        verify(authManager).authenticate(argThat(a ->
                a instanceof UsernamePasswordAuthenticationToken token &&
                        "ivan.horvat".equals(token.getPrincipal()) &&
                        "password123".equals(token.getCredentials())
        ));
    }

    @Test
    void shouldThrowExceptionWhenCredentialsAreWrong() {
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authManager).authenticate(any());

        assertThrows(BadCredentialsException.class,
                () -> authService.login(loginRequest));

        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(tokenProvider, never()).generateAccessToken(any());
    }

    @Test
    void shouldUsePrincipalFromAuthentication() {

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(authManager.authenticate(any())).thenReturn(auth);

        when(tokenProvider.generateAccessToken(userDetails)).thenReturn("access-token");
        when(tokenProvider.generateRefreshToken(userDetails)).thenReturn("refresh-token");

        authService.login(loginRequest);

        verify(auth).getPrincipal();
        verify(tokenProvider).generateAccessToken(userDetails);

        verifyNoInteractions(userDetailsService);
    }

    // --- refresh ---

    @Test
    void shouldReturnNewAccessTokenOnRefresh() {
        when(tokenProvider.extractUsername("valid-refresh-token")).thenReturn("ivan.horvat");
        when(userDetailsService.loadUserByUsername("ivan.horvat")).thenReturn(userDetails);
        when(tokenProvider.isValid("valid-refresh-token", userDetails)).thenReturn(true);
        when(tokenProvider.generateAccessToken(userDetails)).thenReturn("new-access-token");

        RefreshTokenResponseDTO result = authService.refresh("valid-refresh-token");

        assertThat(result.accessToken()).isEqualTo("new-access-token");
        assertThat(result.refreshToken()).isEqualTo("valid-refresh-token");
    }

    @Test
    void shouldReturnSameRefreshTokenOnRefresh() {
        when(tokenProvider.extractUsername("valid-refresh-token")).thenReturn("ivan.horvat");
        when(userDetailsService.loadUserByUsername("ivan.horvat")).thenReturn(userDetails);
        when(tokenProvider.isValid("valid-refresh-token", userDetails)).thenReturn(true);
        when(tokenProvider.generateAccessToken(userDetails)).thenReturn("new-access-token");

        RefreshTokenResponseDTO result = authService.refresh("valid-refresh-token");

        assertThat(result.refreshToken()).isEqualTo("valid-refresh-token");
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenIsInvalid() {
        when(tokenProvider.extractUsername("invalid-refresh-token")).thenReturn("ivan.horvat");
        when(userDetailsService.loadUserByUsername("ivan.horvat")).thenReturn(userDetails);
        when(tokenProvider.isValid("invalid-refresh-token", userDetails)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> authService.refresh("invalid-refresh-token"));

        verify(tokenProvider, never()).generateAccessToken(any());
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenIsExpired() {
        when(tokenProvider.extractUsername("expired-refresh-token"))
                .thenThrow(new JwtException("Token expired"));

        assertThrows(JwtException.class,
                () -> authService.refresh("expired-refresh-token"));

        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(tokenProvider, never()).generateAccessToken(any());
    }
}