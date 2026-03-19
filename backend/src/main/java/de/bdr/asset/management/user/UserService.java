package de.bdr.asset.management.user;

/**
 * User Service
 */
public interface UserService {

    /**
     * Create user in DB.
     * Typically used when user is successfully login for a first time (after it is found in LDAP)
     * @param userRequest - a UserRequestDTO record
     * @return a UserResponseDTO record
     */
    UserDTO createUser(UserDTO userRequest);

    UserDTO updateUser(UserDTO userRequest);
}
