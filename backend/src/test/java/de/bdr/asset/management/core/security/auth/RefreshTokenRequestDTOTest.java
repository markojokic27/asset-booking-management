package de.bdr.asset.management.core.security.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidationWithValidRefreshToken() {
        RefreshTokenRequestDTO request = new RefreshTokenRequestDTO("valid-refresh-token");

        Set<ConstraintViolation<RefreshTokenRequestDTO>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldPassValidationWithNullRefreshToken() {
        RefreshTokenRequestDTO request = new RefreshTokenRequestDTO(null);

        Set<ConstraintViolation<RefreshTokenRequestDTO>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldCreateRecordWithCorrectValue() {
        RefreshTokenRequestDTO request = new RefreshTokenRequestDTO("my-token");

        assertThat(request.refreshToken()).isEqualTo("my-token");
    }

    @Test
    void shouldCreateRecordWithNullValue() {
        RefreshTokenRequestDTO request = new RefreshTokenRequestDTO(null);

        assertThat(request.refreshToken()).isNull();
    }
}