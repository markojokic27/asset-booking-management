package de.bdr.asset.management.core.config.security;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("v1/auth")
@Tag(
        name = "Authentication",
        description = "Endpoints for Authentication. AuthController"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login endpoint", description = "Available to anyone. Returns JWT access and refresh tokens for authenticaton of requests.")
    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @Operation(summary = "Refresh tokens endpoint", description = "Available to anyone. Returns JWT access token if refresh token is valid.")
    @PostMapping("/refresh")
    public RefreshTokenResponseDTO refresh(@RequestBody RefreshTokenRequestDTO request) {
        return authService.refresh(request.refreshToken());
    }

}