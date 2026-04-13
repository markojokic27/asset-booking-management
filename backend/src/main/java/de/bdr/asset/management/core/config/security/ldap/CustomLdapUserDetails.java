package de.bdr.asset.management.core.config.security.ldap;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomLdapUserDetails implements UserDetails {

    private final Long id; // optional (can be null for LDAP users)
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private final String email;
    private final String displayName;
    private final String department;

    @Builder
    public CustomLdapUserDetails(
            Long id,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            String email,
            String displayName,
            String department
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.email = email;
        this.displayName = displayName;
        this.department = department;
    }

    @Override
    public String getUsername() { return username; }

    @Override
    public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}