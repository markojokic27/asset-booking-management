package de.bdr.asset.management.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * User Service
 */
public interface UserService {

    /** CREATE */
    UserResponseDTO createUser(UserRequestDTO userRequest);

    /** READ */
    UserResponseDTO getUserById(Long id);
    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    /** UPDATE */
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequest);

    /** DELETE (Soft) */
    void softDeleteUser(Long id);
}
