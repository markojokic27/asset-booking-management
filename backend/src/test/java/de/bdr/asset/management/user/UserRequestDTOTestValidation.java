package de.bdr.asset.management.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRequestDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidator(){
        ValidatorFactory factory= Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    private UserRequestDTO validDTO(){
        return new UserRequestDTO(
                "ivanivic",
                "ivic",
                "ivan",
                "iivanivic@maurer-electonics.hr",
                "password.123",
                UserRoleEnum.EMPLOYEE,
                UserStatusEnum.ACTIVE,
                5L,
                "antem@maurer-electonics.hr",
                "Some optional notes",
                "ALL"
        );
    }

    private Set<ConstraintViolation<UserRequestDTO>> violationsFor(String field, UserRequestDTO dto){
        return validator.validateProperty(dto, field);
    }

    // All valid fields should produce no validation errors
    @Test
    void validDTO_shouldHaveNoViolations(){
        assertThat(validator.validate(validDTO())).isEmpty();
    }

    //Note

    // Note is empty
    @Test
    void emptyNotes_shouldBeValid(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "", "ALL");
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("notes"));
    }

    // Note is too long
    @Test
    void notesTooLong_shouldFailSize(){
        UserRequestDTO dto = new UserRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr","a".repeat(1001), "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("notes"));
    }

    //Username

    // Username is blank
    @Test
    void blankUsername_shouldFailNotBlank(){
        UserRequestDTO dto=new UserRequestDTO( "", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username is too short
    @Test
    void usernameTooShort_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO( "ab", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username is too long
    @Test
    void usernameTooLong_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO( "a".repeat(51), "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username is null
    @Test
    void usernameNull_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO( null, "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username with invalid characters
    @Test
    void usernameInvalidChars_shouldFailPattern(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic!", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username with allowed characters
    @Test
    void usernameAllowedChars_shouldBeValid(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic48", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Surname

    // Surname is blank
    @Test
    void blankSurname_shouldFailNotBlank(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("surname"));
    }

    // Surname is too long
    @Test
    void surnameTooLong_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "a". repeat(101), "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("surname"));
    }

    // Surname is null
    @Test
    void surnameNull_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", null, "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("surname"));
    }

    // Name

    // Name is blank
    @Test
    void blankName_shouldFailNotBlank(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Name is too long
    @Test
    void nameTooLong_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "a".repeat(101), "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Name is null
    @Test
    void nameNull_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", null, "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    //Email

    // Email is blank
    @Test
    void blankEmail_shouldFailNotBlank(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", "", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    // Email with invalid format
    @Test
    void invalidEmailFormat_shouldFailEmail(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", " ivanivic", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    // Email is null
    @Test
    void emailNull_shouldFailEmail(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", null, "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    // Email is too long
    @Test
    void emailTooLong_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO("ivanivic", "ivic", "ivan", "ivanivic".repeat(255) + "@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    //Password

    //Password is blank
    @Test
    void blankPassword_shouldFailNotBlank (){
        UserRequestDTO dto=new UserRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    //Password is null
    @Test
    void passwordNull_shouldFailNotBlank (){
        UserRequestDTO dto=new UserRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", null, UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    //Password is too short
    @Test
    void passwordTooShort_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "pass1", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("password"));
    }

    //Password is too long
    @Test
    void passwordTooLong_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr",  "p".repeat(51), UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("password"));
    }

    //Password with min lenght
    @Test
    void passwordMinLength_shouldBeValid(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "passw.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).noneMatch(v->v.getPropertyPath().toString().equals("password"));
    }

    //Role

    //Role is null

    @Test
    void nullRole_shouldFailNotNull(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", null , UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("role"));
    }

    //Status

    //Status is null
    @Test
    void nullStatus_shouldFailNotNull(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE , null,  5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("status"));
    }

    //DepartmentId

    //DepartmentId is null
    @Test
    void nullDepartmentId_shouldFailNotNull(){
        UserRequestDTO dto=new UserRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE , UserStatusEnum.ACTIVE,  null, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("departmentId"));
    }

    //Manager email

    // Manager email is blank
    @Test
    void blankManagerEmail_shouldFailNotBlank(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

    // Manager email is null
    @Test
    void managerEmailNull_shouldFailNotBlank(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, null, "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

    // Manager email with invalid format
    @Test
    void invalidManagerEmailFormat_shouldFailEmail(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "a1+", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

    // Manager email is too long
    @Test
    void managerEmailTooLong_shouldFailSize(){
        UserRequestDTO dto=new UserRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "a".repeat(255), "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

}
