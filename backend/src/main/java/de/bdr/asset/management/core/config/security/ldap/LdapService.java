package de.bdr.asset.management.core.config.security.ldap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.List;

@Slf4j
@Service
public class LdapService {

    private final LdapTemplate ldapTemplate;

    public LdapService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void authenticate(String username, String password) {
        boolean authenticated = ldapTemplate.authenticate(
                "ou=users",
                "(uid=" + username + ")",
                password
        );

        if (!authenticated) {
            throw new BadCredentialsException("Invalid LDAP credentials");
        }
    }

    public List<LdapUserDTO> fetchAllUsers() {
        return ldapTemplate.search(
                "ou=users",
                "(objectClass=inetOrgPerson)",
                (AttributesMapper<LdapUserDTO>) attrs -> new LdapUserDTO(
                        get(attrs, "uid"),
                        get(attrs, "cn"),
                        get(attrs, "sn"),
                        get(attrs, "mail"),
                        get(attrs, "userPassword")
                )
        );
    }

    private String get(Attributes attrs, String key) throws NamingException {
        Attribute attr = attrs.get(key);
        if (attr == null) return null;

        Object value = attr.get();

        if (value instanceof byte[] bytes) {
            return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
        }

        return value.toString();
    }

}