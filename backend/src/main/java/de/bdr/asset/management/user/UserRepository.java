package de.bdr.asset.management.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA User Repository */
public interface UserRepository extends JpaRepository<User, Long> {

    /** Fetches an active user by their unique login username. */
    Optional<User> findByUsername(String username);

    /** Resolves a collection of usernames into a corresponding list of user records. */
    List<User> findByUsernameIn(Collection<String> usernames);

    /** Assesses whether an account with the specified corporate email already exists. */
    boolean existsByEmail(String email);

    /** Assesses whether an account with the specified username already exists. */
    boolean existsByUsername(String username);

    /** Retrieves a user profile by identity ID, pre-fetching the department via an entity graph join. */
    @EntityGraph(attributePaths = {"department"})
    Optional<User> findById(Long id);

    /** Retrieves a user matching specific lifecycle states, pre-fetching the department via an entity graph join. */
    @EntityGraph(attributePaths = {"department"})
    Optional<User> findByIdAndStatusIn(Long id, Collection<UserStatusEnum> statuses);

    /** Retrieves a page of all users, pre-fetching departments to optimize database query performance. */
    @EntityGraph(attributePaths = {"department"})
    Page<User> findAll(Pageable pageable);
}
