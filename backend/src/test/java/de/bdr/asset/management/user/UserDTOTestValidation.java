package de.bdr.asset.management.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidator(){
        ValidatorFactory factory= Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    private UserDTO validDTO(){
        return new UserDTO(
                1L,
                "ivanivic",
                "ivic",
                "ivan",
                "iivanivic@maurer-electonics.hr",
                "password.123",
                UserRoleEnum.EMPLOYEE,
                UserStatusEnum.ACTIVE,
                5L,
                "antem@maurer-electonics.hr",
                "text"
        );
    }

    private Set<ConstraintViolation<UserDTO>> violationsFor(String field, UserDTO dto){
        return validator.validateProperty(dto, field);
    }

    // All valid fields should produce no validation errors
    @Test
    void validDTO_shouldHaveNoViolations(){
        assertThat(validator.validate(validDTO())).isEmpty();
    }

    //Note

    // Note is null
    @Test
    void nullNotes_shouldBeValid(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("notes"));
    }

    //Username

    // Username is blank
    @Test
    void blankUsername_shouldFailNotBlank(){
        UserDTO dto=new UserDTO(1L, " ", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username is too short
    @Test
    void usernameTooShort_shouldFailSize(){
        UserDTO dto=new UserDTO(1L, "ab", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username is too long
    @Test
    void usernameTooLong_shouldFailSize(){
        UserDTO dto=new UserDTO(1L, "a".repeat(60), "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username with invalid characters
    @Test
    void usernameInvalidChars_shouldFailPattern(){
        UserDTO dto=new UserDTO(1L, "ivanivic!", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username with allowed characters
    @Test
    void usernameAllowedChars_shouldBeValid(){
        UserDTO dto=new UserDTO(1L, "ivanivic48", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Surname

    // Surname is blank
    @Test
    void blankSurname_shouldFailNotBlank(){
        UserDTO dto=new UserDTO(1L, "ivanivic", " ", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("surname"));
    }

    // Surname is too long
    @Test
    void surnameTooLong_shouldFailSize(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "a". repeat(101), "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("surname"));
    }

    // Name

    // Name is blank
    @Test
    void blankName_shouldFailNotBlank(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", " ", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Name is too long
    @Test
    void nameTooLong_shouldFailSize(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "a".repeat(102), "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    //Email

    // Email is blank
    @Test
    void blankEmail_shouldFailNotBlank(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", " ", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    // Email with invalid format
    @Test
    void invalidEmailFormat_shouldFailEmail(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", " ivanivic", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    // Email is too long
    @Test
    void emailTooLong_shouldFailSize(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic".repeat(255) + "@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    //Password

    //Password is blank
    @Test
    void blankPassword_shouldFailNotBlank (){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", " ", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    //Password is too short
    @Test
    void passwordTooShort_shouldFailSize(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", " pasw", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("password"));
    }

    //Password is too long
    @Test
    void passwordTooLong_shouldFailSize(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr",  "p".repeat(53), UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("password"));
    }

    //Password with min lenght
    @Test
    void passwordMinLength_shouldBeValid(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "passw.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).noneMatch(v->v.getPropertyPath().toString().equals("password"));
    }

    //Role

    //Role is null

    @Test
    void nullRole_shouldFailNotNull(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", null , UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("role"));
    }

    //Status

    //Status is null
    @Test
    void nullStatus_shouldFailNotNull(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE , null,  5L, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("status"));
    }

    //DepartmentId

    //DepartmentId is null
    @Test
    void nullDepartmentId_shouldFailNotNull(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE , UserStatusEnum.ACTIVE,  null, "antem@maurer-electonics.hr", null);
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("departmentId"));
    }

    //Manager email


    // Manager email is blank
    @Test
    void blankManagerEmail_shouldFailNotBlank(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, " ", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

    // Manager email with invalid format
    @Test
    void invalidManagerEmailFormat_shouldFailEmail(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", " ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "a1+", null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

    // Manager email is too long
    @Test
    void managerEmailTooLong_shouldFailSize(){
        UserDTO dto=new UserDTO(1L, "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr".repeat(255) + "@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "a".repeat(260), null);
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }




}
