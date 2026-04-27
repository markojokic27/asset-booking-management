package de.bdr.asset.management.user;

import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
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

    private UserCreateRequestDTO validDTO(){
        return new UserCreateRequestDTO(
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

    private Set<ConstraintViolation<UserCreateRequestDTO>> violationsFor(String field, UserCreateRequestDTO dto){
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
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "", "ALL");
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("notes"));
    }

    // Note is too long
    @Test
    void notesTooLong_shouldFailSize(){
        UserCreateRequestDTO dto = new UserCreateRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr","a".repeat(1001), "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("notes"));
    }

    //Username

    // Username is blank
    @Test
    void blankUsername_shouldFailNotBlank(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username is too short
    @Test
    void usernameTooShort_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ab", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username is too long
    @Test
    void usernameTooLong_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "a".repeat(51), "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username is null
    @Test
    void usernameNull_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( null, "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username with invalid characters
    @Test
    void usernameInvalidChars_shouldFailPattern(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic!", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Username with allowed characters
    @Test
    void usernameAllowedChars_shouldBeValid(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic48", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // Surname

    // Surname is blank
    @Test
    void blankSurname_shouldFailNotBlank(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("surname"));
    }

    // Surname is too long
    @Test
    void surnameTooLong_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "a". repeat(101), "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("surname"));
    }

    // Surname is null
    @Test
    void surnameNull_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", null, "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("surname"));
    }

    // Name

    // Name is blank
    @Test
    void blankName_shouldFailNotBlank(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Name is too long
    @Test
    void nameTooLong_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "a".repeat(101), "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    // Name is null
    @Test
    void nameNull_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", null, "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    //Email

    // Email is blank
    @Test
    void blankEmail_shouldFailNotBlank(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", "", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    // Email with invalid format
    @Test
    void invalidEmailFormat_shouldFailEmail(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", " ivanivic", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    // Email is null
    @Test
    void emailNull_shouldFailEmail(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", null, "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    // Email is too long
    @Test
    void emailTooLong_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO("ivanivic", "ivic", "ivan", "ivanivic".repeat(255) + "@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    //Password

    //Password is blank
    @Test
    void blankPassword_shouldFailNotBlank (){
        UserCreateRequestDTO dto=new UserCreateRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    //Password is null
    @Test
    void passwordNull_shouldFailNotBlank (){
        UserCreateRequestDTO dto=new UserCreateRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", null, UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    //Password is too short
    @Test
    void passwordTooShort_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "pass1", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("password"));
    }

    //Password is too long
    @Test
    void passwordTooLong_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr",  "p".repeat(51), UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("password"));
    }

    //Password with min lenght
    @Test
    void passwordMinLength_shouldBeValid(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "passw.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).noneMatch(v->v.getPropertyPath().toString().equals("password"));
    }

    //Role

    //Role is null

    @Test
    void nullRole_shouldFailNotNull(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", null , UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("role"));
    }

    //Status

    //Status is null
    @Test
    void nullStatus_shouldFailNotNull(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE , null,  5L, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("status"));
    }

    //DepartmentId

    //DepartmentId is null
    @Test
    void nullDepartmentId_shouldFailNotNull(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO("ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE , UserStatusEnum.ACTIVE,  null, "antem@maurer-electonics.hr", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("departmentId"));
    }

    //Manager email

    // Manager email is blank
    @Test
    void blankManagerEmail_shouldFailNotBlank(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

    // Manager email is null
    @Test
    void managerEmailNull_shouldFailNotBlank(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, null, "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

    // Manager email with invalid format
    @Test
    void invalidManagerEmailFormat_shouldFailEmail(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "a1+", "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

    // Manager email is too long
    @Test
    void managerEmailTooLong_shouldFailSize(){
        UserCreateRequestDTO dto=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "a".repeat(255), "Some optional notes", "ALL");
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("managerEmail"));
    }

}
