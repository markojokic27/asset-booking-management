package de.bdr.asset.management.core.config.security;

import de.bdr.asset.management.user.login.LoginRequestDTO;
import de.bdr.asset.management.user.login.LoginResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/v1/auth/login — exchange credentials for tokens
    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    // POST /api/v1/auth/refresh — exchange refresh token for new access token
    @PostMapping("/refresh")
    public RefreshTokenResponseDTO refresh(@RequestBody RefreshTokenRequestDTO request) {
        return authService.refresh(request.refreshToken());
    }

}