package de.bdr.asset.management.asset;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

import de.bdr.asset.management.asset.dtos.AssetRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class AssetRequestDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidator(){
        ValidatorFactory factory=Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    private AssetRequestDTO validDTO(){
        return new AssetRequestDTO(
                "Hp 15",
                1L,
                "Laptop located in room 301",
                AssetStatusEnum.ACTIVE,
                "Room 301"
        );
    }

    private Set<ConstraintViolation<AssetRequestDTO>> violationsFor(String field, AssetRequestDTO dto){
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
        AssetRequestDTO dto=new AssetRequestDTO( "", 1L, "Laptop located in room 301", AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Name is too long
    @Test
    void nameTooLong_shouldFailSize(){
        AssetRequestDTO dto=new AssetRequestDTO("a".repeat(101), 1L, "Laptop located in room 301", AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    //CategoryId

    //CategoryId is null
    @Test
    void nullCategoryId_shouldFailNotNull(){
        AssetRequestDTO dto=new AssetRequestDTO("Hp 15", null, "Laptop located in room 301", AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("categoryId"));
    }

    //Description

    //Description is too long
    @Test
    void descriptionTooLong_shouldFailSize(){
        AssetRequestDTO dto=new AssetRequestDTO("Hp 15", 1L, "L".repeat(256), AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    // Description is null, should be allowed
    @Test
    void nullDescription_shouldBeValid() {
        AssetRequestDTO dto=new AssetRequestDTO( "Hp 15", 1L, null, AssetStatusEnum.ACTIVE, "Room 301");
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    //Status

    //Status is null
    @Test
    void nullStatus_shouldFailNotNull(){
        AssetRequestDTO dto=new AssetRequestDTO("Hp 15", 1L, "Laptop located in room 301", null, "Room 301");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("status"));
    }

    //Location

    // Location is blank
    @Test
    void blankLocation_shouldFailNotBlank(){
        AssetRequestDTO dto=new AssetRequestDTO( "Hp 15", 1L, "Laptop located in room 301", AssetStatusEnum.ACTIVE, "");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("location"));
    }

    // Location is too long
    @Test
    void locationTooLong_shouldFailSize(){
        AssetRequestDTO dto=new AssetRequestDTO("Hp 15", 1L, "Laptop located in room 301", AssetStatusEnum.ACTIVE, "R".repeat(101));
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("location"));
    }

}
