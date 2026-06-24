package de.bdr.asset.management.department;

import de.bdr.asset.management.user.department.dtos.DepartmentUpdateRequestDTO;
import de.bdr.asset.management.user.department.DepartmentEnum;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DepartmentUpdateRequestDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidation(){
        ValidatorFactory factory=Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    //All fields null (PATCH with no changes) should produce no violations
    @Test
    void allNullFields_shouldHaveNoViolations(){
        DepartmentUpdateRequestDTO dto=new DepartmentUpdateRequestDTO(null, null);
        assertThat(validator.validate(dto)).isEmpty();
    }

    //Only name provided
    @Test
    void onlyNameProvided_shouldHaveNoViolations(){
        DepartmentUpdateRequestDTO dto=new DepartmentUpdateRequestDTO(DepartmentEnum.DEVOPS, null);
        assertThat(validator.validate(dto)).isEmpty();
    }

    //Only managerId provided
    @Test
    void onlyManagerIdProvided_shouldHaveNoViolations(){
        DepartmentUpdateRequestDTO dto=new DepartmentUpdateRequestDTO(null, 1L);
        assertThat(validator.validate(dto)).isEmpty();
    }

    //Both fields provided
    @Test
    void bothFieldsProvided_shouldHaveNoViolations(){
        DepartmentUpdateRequestDTO dto=new DepartmentUpdateRequestDTO(DepartmentEnum.DEVOPS, 1L);
        assertThat(validator.validate(dto)).isEmpty();
    }
}
