package de.bdr.asset.management.user;

/**
 * Implementation of User Service
 */
public class UserServiceImpl implements UserService {

    /**
     * Create user in DB.
     * Typically used when user is successfully login for a first time (after it is found in LDAP)
     *
     * @param userRequest - a UserRequestDTO record
     * @return a UserResponseDTO record
     */
    @Override
    public UserDTO createUser(UserDTO userRequest) {

        // TODO Implement...

        return null;
    }

    @Override
    public UserDTO updateUser(UserDTO userRequest) {

        // TODO Implement...

        return null;
    }
}
