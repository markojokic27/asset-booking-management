package de.bdr.asset.management.user;

import de.bdr.asset.management.user.dtos.ChangePasswordRequestDTO;
import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * User Service
 */
public interface UserService {

    /** CREATE */
    UserResponseDTO createUser(UserCreateRequestDTO userRequest);

    /** READ */
    UserResponseDTO getUserById(Long id);
    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    /** UPDATE */
    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO userRequest);

    /**
     * Change user password
     */
    void changePassword(Long id, ChangePasswordRequestDTO changePasswordRequest);

    /** DELETE (Soft) */
    void softDeleteUser(Long id);
}
