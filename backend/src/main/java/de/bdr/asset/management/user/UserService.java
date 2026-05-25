package de.bdr.asset.management.user;

import de.bdr.asset.management.user.dtos.ChangePasswordRequestDTO;
import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Application boundary interface for orchestrating user operations. */
public interface UserService {

    /** Registers a new user account. */
    UserResponseDTO createUser(UserCreateRequestDTO userRequest);

    /** Retrieves an individual user profile by ID. */
    UserResponseDTO getUserById(Long id);

    /** Fetches a paginated list of all users. */
    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    /** Updates details for a user. */
    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO userRequest);

    /** Processes a secure update to a user's password. */
    void changePassword(Long id, ChangePasswordRequestDTO changePasswordRequest);

    /** Flags an account as deleted and triggers related booking cancellations. */
    void softDeleteUser(Long id);
}
