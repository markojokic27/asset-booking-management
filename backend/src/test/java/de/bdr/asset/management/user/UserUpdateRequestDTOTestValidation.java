package de.bdr.asset.management.user;

import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserUpdateRequestDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void allNullFields_shouldBeValid() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(null, null, null, null, null, null, null, null, null);
        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void invalidEmailFormat_shouldFailEmail() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(null, null, "invalid-email", null, null, null, null, null, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void emailTooLong_shouldFailSize() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(null, null, "a".repeat(300) + "@test.com", null, null, null, null, null, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void surnameTooLong_shouldFailSize() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("a".repeat(101), null, null, null, null, null, null, null, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("surname"));
    }

    @Test
    void nameTooLong_shouldFailSize() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(null, "a".repeat(101), null, null, null, null, null, null, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void invalidManagerEmailFormat_shouldFailEmail() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(null, null, null, null, null, null, "not-an-email", null, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

    @Test
    void managerEmailTooLong_shouldFailSize() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(null, null, null, null, null, null, "a".repeat(300) + "@test.com", null, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

    @Test
    void notesTooLong_shouldFailSize() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(null, null, null, null, null, null, null, "a".repeat(1001), null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("notes"));
    }

    @Test
    void benefitTooLong_shouldFailSize() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(null, null, null, null, null, null, null, null, "a".repeat(101));
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("benefit"));
    }
}
