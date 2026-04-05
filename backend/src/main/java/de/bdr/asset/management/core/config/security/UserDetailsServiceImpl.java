package de.bdr.asset.management.core.config.security;

import de.bdr.asset.management.user.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Called by JwtAuthenticationFilter and AuthenticationManager
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        de.bdr.asset.management.user.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Spring Security's User builder constructs a UserDetails implementation.
        // roles("EMPLOYEE") adds "ROLE_EMPLOYEE" — Spring Security always prefixes roles.
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // already BCrypt-hashed in DB
                .roles(user.getRole().name()) // ROLE_EMPLOYEE or ROLE_ADMIN
                .build();
    }
}