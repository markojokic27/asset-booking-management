package de.bdr.asset.management.assetcategory;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class AssetCategoryDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidator(){
        ValidatorFactory factory= Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    private AssetCategoryDTO validDTO(){
        return new AssetCategoryDTO(
                1L,
                "Books",
                "A collection of books available for borrowing within the company library.",
                BookingPeriodEnum.DAY,
                Boolean.TRUE
        );
    }

    private Set<ConstraintViolation<AssetCategoryDTO>> violationsFor(String field, AssetCategoryDTO dto){
        return validator.validateProperty(dto, field);
    }

    //All valid fields should produce no validation
    @Test
    void validDTO_shouldHaveNoViolations(){
        assertThat(validator.validate(validDTO())).isEmpty();
    }

    // Name

    // Name is blank
    @Test
    void blankName_shouldFailNotBlank(){
        AssetCategoryDTO dto=new AssetCategoryDTO(1L, " ", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Name is too long
    @Test
    void nameTooLong_shouldFailSize(){
        AssetCategoryDTO dto=new AssetCategoryDTO(1L, "B".repeat(260), "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    //Description

    //Description is too long
    @Test
    void descriptionTooLong_shouldFailSize(){
        AssetCategoryDTO dto=new AssetCategoryDTO(1L, "Books", "A".repeat(270), BookingPeriodEnum.DAY, Boolean.TRUE);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    // Description is null, should be allowed
    @Test
    void nullDescription_shouldBeValid() {
        AssetCategoryDTO dto = new AssetCategoryDTO(1L, "Books", null, BookingPeriodEnum.DAY, Boolean.TRUE);
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    //Booking period

    //Booking period is null
    @Test
    void nullBookingPeriod_shouldFailNotNull(){
        AssetCategoryDTO dto=new AssetCategoryDTO(1L, "Books", "A collection of books available for borrowing within the company library.", null, Boolean.TRUE);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("bookingPeriod"));
    }

    //Approval

    //Approval is null
    @Test
    void nullApproval_shouldFailNotNull(){
        AssetCategoryDTO dto=new AssetCategoryDTO(1L, "Books", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("approval"));
    }

}
