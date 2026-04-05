package de.bdr.asset.management.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA User Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
