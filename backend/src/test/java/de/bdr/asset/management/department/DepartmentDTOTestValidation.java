package de.bdr.asset.management.department;

import de.bdr.asset.management.user.department.DepartmentDTO;
import de.bdr.asset.management.user.department.DepartmentEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DepartmentDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidation(){
        ValidatorFactory factory=Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    private DepartmentDTO validDTO(){
        return new DepartmentDTO(
           1L,
           DepartmentEnum.DEVOPS,
           2L
        );
    }

    private Set<ConstraintViolation<DepartmentDTO>> violationSet(String field, DepartmentDTO dto){
        return validator.validateProperty(dto, field);
    }

    //All valid fields should produce no validation
    @Test
    void validDTO_shouldHaveNoViolations(){
        assertThat(validator.validate(validDTO())).isEmpty();
    }

    //name

    //Name is null
    @Test
    void nullName_shouldFailNotNull(){
        DepartmentDTO dto=new DepartmentDTO(1L, null, 2L);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    //managerId

    //ManagerId is null
    @Test
    void nullManagerId_shouldFailNotNull(){
        DepartmentDTO dto=new DepartmentDTO(1L, DepartmentEnum.DEVOPS, null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerId"));
    }



}
