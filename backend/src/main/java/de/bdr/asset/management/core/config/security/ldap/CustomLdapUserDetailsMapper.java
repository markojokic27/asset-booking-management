package de.bdr.asset.management.core.config.security.ldap;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomLdapUserDetailsMapper extends LdapUserDetailsMapper {

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx,
                                          String username,
                                          Collection<? extends GrantedAuthority> authorities) {

        // Let parent handle the standard mapping
        UserDetails base = super.mapUserFromContext(ctx, username, authorities);

        // Extract additional attributes
        String email = ctx.getStringAttribute("mail");
        String displayName = ctx.getStringAttribute("cn");
        String department = ctx.getStringAttribute("departmentNumber");

        // Return an enriched UserDetails (use a custom implementation)
        return CustomLdapUserDetails.builder()
                .username(base.getUsername())
                .password(base.getPassword())
                .authorities(base.getAuthorities())
                .email(email)
                .displayName(displayName)
                .department(department)
                .build();
    }
}