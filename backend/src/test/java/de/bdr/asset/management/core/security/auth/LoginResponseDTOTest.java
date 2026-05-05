package de.bdr.asset.management.core.security.auth;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseDTOTest {

    @Test
    void shouldCreateRecordWithCorrectValues() {
        LoginResponseDTO response = new LoginResponseDTO("access-token", "refresh-token");

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void shouldCreateRecordWithNullValues() {
        LoginResponseDTO response = new LoginResponseDTO(null, null);

        assertThat(response.accessToken()).isNull();
        assertThat(response.refreshToken()).isNull();
    }

    @Test
    void shouldCreateRecordWithNullAccessToken() {
        LoginResponseDTO response = new LoginResponseDTO(null, "refresh-token");

        assertThat(response.accessToken()).isNull();
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void shouldCreateRecordWithNullRefreshToken() {
        LoginResponseDTO response = new LoginResponseDTO("access-token", null);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isNull();
    }
}