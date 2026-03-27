package de.bdr.asset.management.user;

import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementation of User Service
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        // TODO Implement logic: map UserRequestDTO -> User, save, map User -> UserResponseDTO
        return null;
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequest) {
        // TODO Implement logic: find User by id, update fields, save, map -> UserResponseDTO
        return null;
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        // TODO Implement logic: find User by id, map -> UserResponseDTO
        return null;
    }

    @Override
    public List<UserResponseDTO> getAllUsers(int pageNumber, int perPage) {
        // TODO Implement logic: use userRepository.findAll(PageRequest.of(...)), map -> List<UserResponseDTO>
        return List.of(); // dummy empty list for now
    }

    @Override
    public UserResponseDTO deleteUser(Long id, String status, String note) {
        // TODO Implement logic: find User, set status = INACTIVE, save, map -> UserResponseDTO
        return null;
    }
}
