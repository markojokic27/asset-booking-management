package de.bdr.asset.management.assetcategory;

import de.bdr.asset.management.assetcategory.dto.AssetCategoryRequestDTO;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class AssetCategoryRequestDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidator(){
        ValidatorFactory factory= Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    private AssetCategoryRequestDTO validDTO(){
        return new AssetCategoryRequestDTO(
                "Books",
                "A collection of books available for borrowing within the company library.",
                BookingPeriodEnum.DAY,
                Boolean.TRUE
        );
    }

    private Set<ConstraintViolation<AssetCategoryRequestDTO>> violationsFor(String field, AssetCategoryRequestDTO dto){
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
        AssetCategoryRequestDTO dto=new AssetCategoryRequestDTO( "", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Name is too long
    @Test
    void nameTooLong_shouldFailSize(){
        AssetCategoryRequestDTO dto=new AssetCategoryRequestDTO("B".repeat(101), "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    //Description

    //Description is too long
    @Test
    void descriptionTooLong_shouldFailSize(){
        AssetCategoryRequestDTO dto=new AssetCategoryRequestDTO( "Books", "A".repeat(256), BookingPeriodEnum.DAY, Boolean.TRUE);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    // Description is null, should be allowed
    @Test
    void nullDescription_shouldBeValid() {
        AssetCategoryRequestDTO dto = new AssetCategoryRequestDTO( "Books", null, BookingPeriodEnum.DAY, Boolean.TRUE);
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    //Booking period

    //Booking period is null
    @Test
    void nullBookingPeriod_shouldFailNotNull(){
        AssetCategoryRequestDTO dto=new AssetCategoryRequestDTO( "Books", "A collection of books available for borrowing within the company library.", null, Boolean.TRUE);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("bookingPeriod"));
    }

    //Approval

    //Approval is null
    @Test
    void nullApproval_shouldFailNotNull(){
        AssetCategoryRequestDTO dto=new AssetCategoryRequestDTO( "Books", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("approval"));
    }

}
