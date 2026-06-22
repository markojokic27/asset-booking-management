package de.bdr.asset.management.user;

import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.dtos.ChangePasswordRequestDTO;
import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;

/** Application boundary interface for orchestrating user operations. */
public interface UserService {

    /**
     * Registers a new user account.
     *
     * @param userRequest Data blueprint containing validation parameters to provision the account.
     * @return Data view representing the summary of the newly created profile.
     * @throws DuplicateResourceException If the requested username or email is already taken.
     * @throws ResourceNotFoundException If the assigned department ID does not exist.
     */
    UserResponseDTO createUser(UserCreateRequestDTO userRequest);

    /**
     * Retrieves an individual user profile by ID.
     *
     * @param id Unique identity index lookup key.
     * @return Data view representing the requested user profile summary.
     * @throws ResourceNotFoundException If the specified user identity is missing.
     */
    UserResponseDTO getUserById(Long id);

    /**
     * Fetches a paginated list of all users.
     *
     * @param pageable Pagination and sorting criteria.
     * @return A paginated data window containing matching outbound user profiles.
     */
    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    /**
     * Updates details for a user.
     *
     * @param id Unique identity index lookup key of the target account.
     * @param userRequest Data contract containing fields permitted for partial profile updates.
     * @return Data view representing the updated user profile summary.
     * @throws ResourceNotFoundException If the targeted user identity does not exist.
     */
    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO userRequest);

    /**
     * Processes a secure update to a user's password.
     *
     * @param id Unique identity index lookup key of the target account.
     * @param changePasswordRequest Data transfer block containing current and new credentials.
     * @throws ResourceNotFoundException If the specified user identity does not exist.
     * @throws BadCredentialsException If the provided current password validation check fails.
     */
    void changePassword(Long id, ChangePasswordRequestDTO changePasswordRequest);

    /**
     * Flags an account as deleted and triggers related booking cancellations.
     *
     * @param id Unique identity index lookup key of the profile to archive.
     * @throws ResourceNotFoundException If the specified user identity does not exist.
     */
    void softDeleteUser(Long id);

    User getActiveOrStudentUserById(Long id);
}
