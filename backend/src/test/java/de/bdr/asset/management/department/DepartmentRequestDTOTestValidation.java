package de.bdr.asset.management.department;

import de.bdr.asset.management.user.department.dtos.DepartmentRequestDTO;
import de.bdr.asset.management.user.department.DepartmentEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DepartmentRequestDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidation(){
        ValidatorFactory factory=Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    private DepartmentRequestDTO validDTO(){
        return new DepartmentRequestDTO(
           DepartmentEnum.DEVOPS,
           2L
        );
    }

    private Set<ConstraintViolation<DepartmentRequestDTO>> violationSet(String field, DepartmentRequestDTO dto){
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
        DepartmentRequestDTO dto=new DepartmentRequestDTO(null, 2L);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    //managerId

    //ManagerId is null (optional field — should be allowed)
    @Test
    void nullManagerId_shouldBeAllowed(){
        DepartmentRequestDTO dto=new DepartmentRequestDTO( DepartmentEnum.DEVOPS, null);
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("managerId"));
    }



}
