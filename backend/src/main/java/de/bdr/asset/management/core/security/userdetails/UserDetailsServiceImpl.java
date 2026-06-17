package de.bdr.asset.management.core.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.bdr.asset.management.user.UserRepository;
import de.bdr.asset.management.user.UserStatusEnum;

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

        if (user.getStatus() != UserStatusEnum.ACTIVE && user.getStatus() != UserStatusEnum.STUDENT) {
            throw new UsernameNotFoundException("User account is not active: " + username);
        }

        return new CustomUserDetails(user);   // return custom UserDetails
    }
}