package de.bdr.asset.management.asset;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AssetDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidator(){
        ValidatorFactory factory=Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    private AssetDTO validDTO(){
        return new AssetDTO(
                1L,
                "Hp 15",
                1L,
                "Laptop located in room 301",
                "QR-LAPTOP-001",
                AssetStatusEnum.ACTIVE,
                "Room 301"

        );


    }

    private Set<ConstraintViolation<AssetDTO>> violationsFor(String field, AssetDTO dto){
        return validator.validateProperty(dto, field);
    }

    // All valid fields should produce no validation errors
    @Test
    void validDTO_shouldHaveNoViolations(){
        assertThat(validator.validate(validDTO())).isEmpty();
    }

    // Name

    // Name is blank
    @Test
    void blankName_shouldFailNotBlank(){
        AssetDTO dto=new AssetDTO(1L, " ", 1L, "Laptop located in room 301", "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Name is too long
    @Test
    void nameTooLong_shouldFailSize(){
        AssetDTO dto=new AssetDTO(1L, "a".repeat(101), 1L, "Laptop located in room 301", "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    //CategoryId

    //CategoryId is null
    @Test
    void nullCategoryId_shouldFailNotNull(){
        AssetDTO dto=new AssetDTO(1L, "Hp 15", null, "Laptop located in room 301", "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("categoryId"));
    }

    //Description

    //Description is too long
    @Test
    void descriptionTooLong_shouldFailSize(){
        AssetDTO dto=new AssetDTO(1L, "Hp 15", 1L, "L".repeat(278), "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    // Description is null, should be allowed
    @Test
    void nullDescription_shouldBeValid() {
        AssetDTO dto=new AssetDTO(1L, "Hp 15", 1L, null, "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    // QR code

    // Code is blank
    @Test
    void blankCode_shouldFailNotBlank(){
        AssetDTO dto=new AssetDTO(1L, "Hp 15", 1L, "Laptop located in room 301", " ", AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("code"));
    }

    // Code is too long
    @Test
    void codeTooLong_shouldFailSize(){
        AssetDTO dto=new AssetDTO(1L, "Hp 15", 1L, "Laptop located in room 301", "Q".repeat(20005), AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("code"));
    }

    //Status

    //Status is null
    @Test
    void nullStatus_shouldFailNotNull(){
        AssetDTO dto=new AssetDTO(1L, "Hp 15", 1L, "Laptop located in room 301", "QR-LAPTOP-001", null, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("status"));
    }

    //Location

    // Location is blank
    @Test
    void blankLocation_shouldFailNotBlank(){
        AssetDTO dto=new AssetDTO(1L, "Hp 15", 1L, "Laptop located in room 301", "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, " ");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("location"));
    }

    // Location is too long
    @Test
    void locationTooLong_shouldFailSize(){
        AssetDTO dto=new AssetDTO(1L, "Hp 15", 1L, "Laptop located in room 301", "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "R".repeat(300));
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("location"));
    }

}
