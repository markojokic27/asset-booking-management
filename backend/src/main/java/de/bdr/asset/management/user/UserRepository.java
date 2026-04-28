package de.bdr.asset.management.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA User Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByUsernameIn(Collection<String> usernames);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByUsernameAndIdNot(String username, Long id);

    @EntityGraph(attributePaths = {"department"})
    Optional<User> findById(Long id);

    @EntityGraph(attributePaths = {"department"})
    Optional<User> findByIdAndStatusIn(Long id, Collection<UserStatusEnum> statuses);

    @EntityGraph(attributePaths = {"department"})
    Page<User> findAll(Pageable pageable);
}
