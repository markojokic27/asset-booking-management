package de.bdr.asset.management.user;

import de.bdr.asset.management.user.dtos.ChangePasswordRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangePasswordRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validDto_shouldHaveNoViolations() {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO("currentPass123", "newPass123");
        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void nullCurrentPassword_shouldFailNotNull() {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO(null, "newPass123");
        Set<ConstraintViolation<ChangePasswordRequestDTO>> violations = validator.validateProperty(dto, "currentPassword");
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("currentPassword"));
    }

    @Test
    void nullNewPassword_shouldFailNotNull() {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO("currentPass123", null);
        Set<ConstraintViolation<ChangePasswordRequestDTO>> violations = validator.validateProperty(dto, "newPassword");
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("newPassword"));
    }

    @Test
    void newPasswordTooShort_shouldFailSize() {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO("currentPass123", "short");
        Set<ConstraintViolation<ChangePasswordRequestDTO>> violations = validator.validateProperty(dto, "newPassword");
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("newPassword"));
    }
}
