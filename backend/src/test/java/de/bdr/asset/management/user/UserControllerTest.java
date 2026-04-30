package de.bdr.asset.management.user;

import de.bdr.asset.management.user.dtos.ChangePasswordRequestDTO;
import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    /** CREATE */
    @Test
    void createUser_validRequest_returnsCreatedStatus(){
        UserCreateRequestDTO request=new UserCreateRequestDTO( "ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", "password.123", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null, "ALL");
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
        UserResponseDTO response = new UserResponseDTO(
                1L,
                "ivanivic",
                "ivic",
                "ivan",
                "ivanivic@maurer-electonics.hr",
                UserRoleEnum.EMPLOYEE,
                UserStatusEnum.ACTIVE,
                5L,
                "antem@maurer-electonics.hr",
                null,
                "ALL");

        List<UserResponseDTO> list = List.of(response);
        Page<UserResponseDTO> page = new PageImpl<>(list);

        when(userService.getAllUsers(any(Pageable.class)))
                .thenReturn(page);

        ResponseEntity<Page<UserResponseDTO>> result =
                userController.getAllUsers(PageRequest.of(0, 10));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert(result.getBody() != null);
        assertThat(result.getBody().getContent())
                .hasSize(1)
                .contains(response);
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
        UserUpdateRequestDTO request=new UserUpdateRequestDTO(UserStatusEnum.LEFT_COMPANY, "test notes", "SOME");
        UserResponseDTO response=new UserResponseDTO( 1L,"ivanivic", "ivic", "ivan", "ivanivic@maurer-electonics.hr", UserRoleEnum.EMPLOYEE, UserStatusEnum.ACTIVE, 5L, "antem@maurer-electonics.hr", null, "ALL");

        when(userService.updateUser(1L, request)).thenReturn(response);

        ResponseEntity<UserResponseDTO> result = userController.updateUser(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** DELETE */
    @Test
    void deleteUser_returnsNoContent() {

        Long userId = 1L;

        doNothing().when(userService).softDeleteUser(userId);

        ResponseEntity<Void> result = userController.deleteUser(userId);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(result.getBody()).isNull();

        verify(userService).softDeleteUser(userId);
    }

    /** CHANGE PASSWORD */
    @Test
    void changePassword_validRequest_returnsNoContent() {

        Long userId = 1L;
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("oldPass123", "newPass123");

        doNothing().when(userService).changePassword(userId, request);

        ResponseEntity<Void> result = userController.changePassword(userId, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(result.getBody()).isNull();

        verify(userService).changePassword(userId, request);
    }
}
