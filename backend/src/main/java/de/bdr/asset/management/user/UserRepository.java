package de.bdr.asset.management.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA User Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
