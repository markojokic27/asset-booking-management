package de.bdr.asset.management.core.config.security.ldap;

import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LdapService {

    private final LdapTemplate ldapTemplate;

    public List<LdapUserDTO> fetchAllUsers() {
        return ldapTemplate.search(
                "ou=users",
                "(objectClass=inetOrgPerson)",
                (Attributes attrs) -> mapToDto(attrs)
        );
    }

    private LdapUserDTO mapToDto(Attributes attrs) throws NamingException {
        return new LdapUserDTO(
                getString(attrs, "uid"),
                getString(attrs, "givenName"),
                getString(attrs, "sn"),
                getString(attrs, "mail"),
                getPassword(attrs),
                getString(attrs, "departmentNumber"),
                getString(attrs, "manager"),
                getString(attrs, "employeeType"),
                getString(attrs, "title")
        );
    }

    private String getString(Attributes attrs, String key) throws NamingException {
        Attribute attr = attrs.get(key);
        return attr != null ? attr.get().toString() : null;
    }

    private String getPassword(Attributes attrs) throws NamingException {
        Attribute attr = attrs.get("userPassword");
        if (attr == null) return null;

        Object value = attr.get();

        if (value instanceof byte[] bytes) {
            return new String(bytes, StandardCharsets.UTF_8);
        }

        return value.toString();
    }
}