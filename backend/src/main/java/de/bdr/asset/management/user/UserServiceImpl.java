package de.bdr.asset.management.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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
        return new UserResponseDTO(
            1L,
            userRequest.username(),
            userRequest.surname(),
            userRequest.name(),
            userRequest.email(),
            userRequest.role(),
            userRequest.status(),
            userRequest.departmentId(),
            userRequest.managerEmail(),
            userRequest.notes()
        );
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequest) {
        // TODO Implement logic: find User by id, update fields, save, map -> UserResponseDTO
        return new UserResponseDTO(
            1L,
            userRequest.username(),
            userRequest.surname(),
            userRequest.name(),
            userRequest.email(),
            userRequest.role(),
            userRequest.status(),
            userRequest.departmentId(),
            userRequest.managerEmail(),
            userRequest.notes()
        );
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        // TODO Implement logic: find User by id, map -> UserResponseDTO
        return new UserResponseDTO(
            1L,
            "userRequest.username()",
            "userRequest.surname()",
            "userRequest.name()",
            "userRequest.email()",
            UserRoleEnum.EMPLOYEE,
            UserStatusEnum.ACTIVE,
            1L,
            "userRequest.managerEmail()",
            "userRequest.notes()"
        );
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        // TODO Implement logic: use userRepository.findAll(PageRequest.of(...)), map -> List<UserResponseDTO>
        List<UserResponseDTO> dummyList = new ArrayList<>();
        dummyList.add(
            new UserResponseDTO(
                1L,
                "Username 1",
                "Surname 1",
                "Name 1",
                "Email 1",
                UserRoleEnum.EMPLOYEE,
                UserStatusEnum.ACTIVE,
                1L,
                "Manager Email 1",
                "Notes 1"
            )
        );

        dummyList.add(
            new UserResponseDTO(
                2L,
                "Username 2",
                "Surname 2",
                "Name 2",
                "Email 2",
                UserRoleEnum.EMPLOYEE,
                UserStatusEnum.ACTIVE,
                2L,
                "Manager Email 2",
                "Notes 2"
            )
        );

        return dummyList;
    }

    @Override
    public UserResponseDTO deleteUser(Long id, String status, String note) {
        // TODO Implement logic: find User, set status = INACTIVE, save, map -> UserResponseDTO
        return null;
    }
}
