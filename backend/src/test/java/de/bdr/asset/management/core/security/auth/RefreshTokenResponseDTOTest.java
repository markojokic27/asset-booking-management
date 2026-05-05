package de.bdr.asset.management.core.security.auth;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenResponseDTOTest {

    @Test
    void shouldCreateRecordWithCorrectValues() {
        RefreshTokenResponseDTO response = new RefreshTokenResponseDTO("access-token", "refresh-token");

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void shouldCreateRecordWithNullValues() {
        RefreshTokenResponseDTO response = new RefreshTokenResponseDTO(null, null);

        assertThat(response.accessToken()).isNull();
        assertThat(response.refreshToken()).isNull();
    }

    @Test
    void shouldCreateRecordWithNullAccessToken() {
        RefreshTokenResponseDTO response = new RefreshTokenResponseDTO(null, "refresh-token");

        assertThat(response.accessToken()).isNull();
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void shouldCreateRecordWithNullRefreshToken() {
        RefreshTokenResponseDTO response = new RefreshTokenResponseDTO("access-token", null);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isNull();
    }
}