package de.bdr.asset.management.user;

import java.util.List;

/**
 * User Service
 */
public interface UserService {

    /** CREATE */
    UserResponseDTO createUser(UserRequestDTO userRequest);

    /** READ */
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();

    /** UPDATE */
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequest);

    /** DELETE (Soft) */
    UserResponseDTO deleteUser(Long id, String status, String note);
}
