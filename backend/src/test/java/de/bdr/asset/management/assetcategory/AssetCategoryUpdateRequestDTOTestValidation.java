package de.bdr.asset.management.assetcategory;

import de.bdr.asset.management.assetcategory.dto.AssetCategoryUpdateRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AssetCategoryUpdateRequestDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // All fields null (PATCH with no changes) should produce no violations
    @Test
    void allNullFields_shouldHaveNoViolations() {
        AssetCategoryUpdateRequestDTO dto = new AssetCategoryUpdateRequestDTO(null, null, null, null);
        assertThat(validator.validate(dto)).isEmpty();
    }

    // Name too long
    @Test
    void nameTooLong_shouldFailSize() {
        AssetCategoryUpdateRequestDTO dto = new AssetCategoryUpdateRequestDTO(
                "B".repeat(101), null, null, null
        );
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Description too long
    @Test
    void descriptionTooLong_shouldFailSize() {
        AssetCategoryUpdateRequestDTO dto = new AssetCategoryUpdateRequestDTO(
                "Books", "A".repeat(256), null, null
        );
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }
}
