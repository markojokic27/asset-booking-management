package de.bdr.asset.management.user;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** READ ALL */
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            Pageable pageable
    ) {
        log.info("Received GET request to fetch users with pagination: " +
                        "Page number: {} | Page size: {} | Sort: {}",
                        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()
        );

        Page<UserResponseDTO> users = userService.getAllUsers(pageable);

        log.debug("Successfully processed GET request for all users");

        return ResponseEntity.ok(users);
    }

    /** CREATE */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO userRequest
    ) {
        log.info("Received POST request to create a new user for department id: {}", userRequest.departmentId());

        // TODO: restrict to Admin if needed
        UserResponseDTO createdUser = userService.createUser(userRequest);

        log.debug("Successfully processed POST request. Created user with id: {}", createdUser.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /** READ BY ID */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id
    ) {
        log.info("Received GET request to fetch user with id: {}", id);

        UserResponseDTO user = userService.getUserById(id);

        log.debug("Successfully processed GET request for user id: {}", id);

        return ResponseEntity.ok(user);
    }

    /** UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO userRequest
    ) {
        log.info("Received PUT request to update user with id: {}", id);

        UserResponseDTO updatedUser = userService.updateUser(id, userRequest);

        log.debug("Successfully processed PUT request for user id: {}", id);

        return ResponseEntity.ok(updatedUser);
    }

    /** Soft DELETE */
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestBody Map<String, String> noteBody
    ) {
        log.info("Received PATCH request to delete user with id: {}", id);

        // The note is optional in the body
        String note = noteBody.getOrDefault("note", "");
        UserResponseDTO deactivatedUser = userService.deleteUser(id, status, note);

        log.debug("Successfully processed PATCH request for user id: {}", id);

        return ResponseEntity.ok(deactivatedUser);
    }
}