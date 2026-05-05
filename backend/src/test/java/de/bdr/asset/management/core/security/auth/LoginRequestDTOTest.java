package de.bdr.asset.management.core.security.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidationWithValidData() {
        LoginRequestDTO request = new LoginRequestDTO("ivan.horvat", "password123");

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    // --- username ---

    @Test
    void shouldFailWhenUsernameIsNull() {
        LoginRequestDTO request = new LoginRequestDTO(null, "password123");

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> "username".equals(v.getPropertyPath().toString()));
    }

    @Test
    void shouldFailWhenUsernameIsTooShort() {
        LoginRequestDTO request = new LoginRequestDTO("ab", "password123");

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> "username".equals(v.getPropertyPath().toString()));
    }

    @Test
    void shouldFailWhenUsernameIsTooLong() {
        String longUsername = "a".repeat(51);
        LoginRequestDTO request = new LoginRequestDTO(longUsername, "password123");

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> "username".equals(v.getPropertyPath().toString()));
    }

    @Test
    void shouldPassWhenUsernameIsMinLength() {
        LoginRequestDTO request = new LoginRequestDTO("abc", "password123");

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldPassWhenUsernameIsMaxLength() {
        String maxUsername = "a".repeat(50);
        LoginRequestDTO request = new LoginRequestDTO(maxUsername, "password123");

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    // --- password ---

    @Test
    void shouldFailWhenPasswordIsNull() {
        LoginRequestDTO request = new LoginRequestDTO("ivan.horvat", null);

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> "password".equals(v.getPropertyPath().toString()));
    }

    @Test
    void shouldFailWhenPasswordIsTooShort() {
        LoginRequestDTO request = new LoginRequestDTO("ivan.horvat", "pass");

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> "password".equals(v.getPropertyPath().toString()));
    }

    @Test
    void shouldFailWhenPasswordIsTooLong() {
        String longPassword = "a".repeat(51);
        LoginRequestDTO request = new LoginRequestDTO("ivan.horvat", longPassword);

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> "password".equals(v.getPropertyPath().toString()));
    }

    @Test
    void shouldPassWhenPasswordIsMinLength() {
        LoginRequestDTO request = new LoginRequestDTO("ivan.horvat", "password");

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldPassWhenPasswordIsMaxLength() {
        String maxPassword = "a".repeat(50);
        LoginRequestDTO request = new LoginRequestDTO("ivan.horvat", maxPassword);

        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }
}