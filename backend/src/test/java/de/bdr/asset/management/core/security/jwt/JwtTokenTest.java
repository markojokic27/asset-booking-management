package de.bdr.asset.management.core.security.jwt;

import de.bdr.asset.management.core.security.userdetails.CustomUserDetails;
import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenTest {

    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetails userDetails;

    private static final String SECRET = Base64.getEncoder().encodeToString(
            "test-secret-key-minimum-256-bits-long!!".getBytes()
    );

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(SECRET);
        jwtProperties.setAccessTokenExpiry(900L);
        jwtProperties.setRefreshTokenExpiry(604800L);

        jwtTokenProvider = new JwtTokenProvider(jwtProperties);

        User user = new User();
        user.setId(1L);
        user.setUsername("ivan.horvat");
        user.setPassword("password123");
        user.setName("Ivan");
        user.setSurname("Horvat");
        user.setEmail("ivan@example.com");
        user.setRole(UserRoleEnum.EMPLOYEE);
        user.setBenefit("ALL");
        user.setStatus(UserStatusEnum.ACTIVE);

        userDetails = new CustomUserDetails(user);
    }

    // --- generateAccessToken ---

    @Test
    void shouldGenerateAccessToken() {
        String token = jwtTokenProvider.generateAccessToken(userDetails);
        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    void shouldExtractUsernameFromAccessToken() {
        String token = jwtTokenProvider.generateAccessToken(userDetails);
        String username = jwtTokenProvider.extractUsername(token);
        assertThat(username).isEqualTo("ivan.horvat");
    }

    @Test
    void shouldValidateAccessToken() {
        String token = jwtTokenProvider.generateAccessToken(userDetails);
        assertThat(jwtTokenProvider.isValid(token, userDetails)).isTrue();
    }

    // --- generateRefreshToken ---

    @Test
    void shouldGenerateRefreshToken() {
        String token = jwtTokenProvider.generateRefreshToken(userDetails);
        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    void shouldExtractUsernameFromRefreshToken() {
        String token = jwtTokenProvider.generateRefreshToken(userDetails);
        String username = jwtTokenProvider.extractUsername(token);
        assertThat(username).isEqualTo("ivan.horvat");
    }

    @Test
    void shouldValidateRefreshToken() {
        String token = jwtTokenProvider.generateRefreshToken(userDetails);
        assertThat(jwtTokenProvider.isValid(token, userDetails)).isTrue();
    }

    // --- isValid ---

    @Test
    void shouldReturnFalseForExpiredToken() throws InterruptedException {
        JwtProperties shortExpiry = new JwtProperties();
        shortExpiry.setSecret(SECRET);
        shortExpiry.setAccessTokenExpiry(1L);
        shortExpiry.setRefreshTokenExpiry(1L);

        JwtTokenProvider shortLivedProvider = new JwtTokenProvider(shortExpiry);
        String token = shortLivedProvider.generateAccessToken(userDetails);

        Thread.sleep(2000);

        assertThrows(Exception.class,
                () -> shortLivedProvider.isValid(token, userDetails));
    }

    @Test
    void shouldReturnFalseForWrongUsername() {
        String token = jwtTokenProvider.generateAccessToken(userDetails);

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("other.user");
        otherUser.setPassword("password123");
        otherUser.setName("Other");
        otherUser.setSurname("User");
        otherUser.setEmail("other@example.com");
        otherUser.setRole(UserRoleEnum.EMPLOYEE);
        otherUser.setBenefit("ALL");
        otherUser.setStatus(UserStatusEnum.ACTIVE);

        CustomUserDetails otherUserDetails = new CustomUserDetails(otherUser);

        assertThat(jwtTokenProvider.isValid(token, otherUserDetails)).isFalse();
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        assertThrows(Exception.class,
                () -> jwtTokenProvider.extractUsername("invalid.token.value"));
    }

    @Test
    void shouldThrowExceptionForTamperedToken() {
        String token = jwtTokenProvider.generateAccessToken(userDetails);
        String tamperedToken = token + "tampered";

        assertThrows(Exception.class,
                () -> jwtTokenProvider.extractUsername(tamperedToken));
    }

    // --- access vs refresh token ---

    @Test
    void shouldGenerateDifferentAccessAndRefreshTokens() {
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        assertThat(accessToken).isNotEqualTo(refreshToken);
    }
}