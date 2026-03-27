package de.bdr.asset.management.user;

import java.util.List;

/**
 * User Service
 */
public interface UserService {

    // Create a new user
    UserResponseDTO createUser(UserRequestDTO userRequest);

    // Update an existing user by ID
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequest);

    // Get a user by ID
    UserResponseDTO getUserById(Long id);

    // Get all users with pagination
    List<UserResponseDTO> getAllUsers(int pageNumber, int perPage);
    
    // Soft delete / deactivate a user
    UserResponseDTO deleteUser(Long id, String status, String note);
}
