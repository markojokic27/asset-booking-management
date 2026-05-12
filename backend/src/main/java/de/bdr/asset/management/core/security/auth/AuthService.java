package de.bdr.asset.management.core.security.auth;

import de.bdr.asset.management.core.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public AuthService(AuthenticationManager authManager, JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        // Delegates to DaoAuthenticationProvider, which calls UserDetailsService
        // and verifies the password against the BCrypt hash in the database.
        // Throws BadCredentialsException if credentials are wrong.
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return new LoginResponseDTO(
                tokenProvider.generateAccessToken(userDetails),
                tokenProvider.generateRefreshToken(userDetails)
        );
    }

    public RefreshTokenResponseDTO refresh(String refreshToken) {
        // Extract and validate the refresh token
        String username = tokenProvider.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!tokenProvider.isValid(refreshToken, userDetails)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        // Issue a fresh access token; the refresh token is returned unchanged
        // (until it expires, at which point the user must re-authenticate with credentials)
        return new RefreshTokenResponseDTO(
                tokenProvider.generateAccessToken(userDetails),
                refreshToken);
    }

}