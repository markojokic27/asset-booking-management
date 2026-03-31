package de.bdr.asset.management.user;

import de.bdr.asset.management.user.department.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    /** CREATE */
    @Test
    void createUser_validRequest_returnsCreatedStatus(){
        UserRequestDTO request=new UserRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null, "ALL");
        UserResponseDTO response=new UserResponseDTO( 1L,"ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr",  UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null, "ALL");

        when(userService.createUser(request)).thenReturn(response);

        ResponseEntity<UserResponseDTO> result = userController.createUser(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        verify(userService).createUser(request);

    }

    /** READ ALL */
    @Test
    void getAllUsers_returnsOkWithList(){
        UserResponseDTO response=new UserResponseDTO( 1L,"ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null, "ALL");

        List<UserResponseDTO> list = List.of(response);
        when(userService.getAllUsers()).thenReturn(list);

        ResponseEntity<List<UserResponseDTO>> result = userController.getAllUsers();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).hasSize(1).contains(response);
    }

    /** READ BY ID */
    @Test
    void getUserById_returnsOkWithUser(){
        UserResponseDTO response=new UserResponseDTO( 1L,"ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr",  UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null, "ALL");

        when(userService.getUserById(1L)).thenReturn(response);

        ResponseEntity<UserResponseDTO> result = userController.getUserById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** UPDATE */
    @Test
    void updateUser_returnsOkWithUpdatesdUser(){
        UserRequestDTO request=new UserRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null, "ALL");
        UserResponseDTO response=new UserResponseDTO( 1L,"ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null, "ALL");

        when(userService.updateUser(1L, request)).thenReturn(response);

        ResponseEntity<UserResponseDTO> result = userController.updateUser(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** DELETE */
    @Test
    void deleteUser_returnsNoContent() {

        ResponseEntity<Void> result = userController.deleteUser(1L, UserStatusEnum.ACTIVE);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(result.getBody()).isNull();
        verify(userService).deleteUser(1L, UserStatusEnum.ACTIVE);
    }


}
