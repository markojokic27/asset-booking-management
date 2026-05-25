package de.bdr.asset.management.user;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.user.dtos.ChangePasswordRequestDTO;
import de.bdr.asset.management.user.dtos.UserCreateRequestDTO;
import de.bdr.asset.management.user.dtos.UserResponseDTO;
import de.bdr.asset.management.user.dtos.UserUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/** User REST Controller. */
@Slf4j
@RestController
@RequestMapping("v1/users")
@Tag(
        name = "Users",
        description = "Endpoints for Users. UserController"
)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Read list of users", description = "Only available to users with role: ADMIN. Takes Pageable object.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
           @ParameterObject Pageable pageable
    ) throws IllegalArgumentException 
    {
        Page<UserResponseDTO> users = userService.getAllUsers(pageable);

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Create user account", description = "Available to everyone, used for registering users.")
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserCreateRequestDTO userRequest
    ) throws DuplicateResourceException, ResourceNotFoundException
    {
        UserResponseDTO createdUser = userService.createUser(userRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Read user details", description = "Only available to users with role: ADMIN or owner of the account")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id
    ) throws ResourceNotFoundException
    {
        UserResponseDTO user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user", description = "Only available to users with role ADMIN or owner of the account")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO userUpdateRequest
    ) throws ResourceNotFoundException
    {
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdateRequest);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Change user password ", description = "Users passwords can be changed only by themselves")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("#id == authentication.principal.id")
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody @Valid ChangePasswordRequestDTO request) {

        userService.changePassword(id, request);
        return ResponseEntity.noContent().build(); // 204 No Content is standard for successful updates with nobody
    }

    @Operation(summary = "Soft delete user", description = "Only available to users with role: ADMIN or owners of the account")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) throws ResourceNotFoundException
    {
        userService.softDeleteUser(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}