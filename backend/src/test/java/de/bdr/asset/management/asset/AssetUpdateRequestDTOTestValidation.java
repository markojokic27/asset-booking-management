package de.bdr.asset.management.asset;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

import de.bdr.asset.management.asset.dtos.AssetUpdateRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class AssetUpdateRequestDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidator(){
        ValidatorFactory factory=Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    // All null fields should be valid (all fields optional)
    @Test
    void validDTO_allNull_shouldHaveNoViolations(){
        AssetUpdateRequestDTO dto = new AssetUpdateRequestDTO(null, null, null, null, null);
        assertThat(validator.validate(dto)).isEmpty();
    }

    // Name is too long
    @Test
    void nameTooLong_shouldFailSize(){
        AssetUpdateRequestDTO dto = new AssetUpdateRequestDTO("a".repeat(101), 1L, null, null, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Description is too long
    @Test
    void descriptionTooLong_shouldFailSize(){
        AssetUpdateRequestDTO dto = new AssetUpdateRequestDTO(null, null, "L".repeat(256), null, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    // Location is too long
    @Test
    void locationTooLong_shouldFailSize(){
        AssetUpdateRequestDTO dto = new AssetUpdateRequestDTO(null, null, null, null, "R".repeat(101));
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("location"));
    }
}
