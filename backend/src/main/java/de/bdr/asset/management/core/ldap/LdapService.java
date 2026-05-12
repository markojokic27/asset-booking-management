package de.bdr.asset.management.core.ldap;

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

    /**
     * Low-level LDAP access service.
     *
     * Responsibility:
     * - Queries LDAP directory
     * - Maps LDAP attributes into application DTOs
     *
     */
    private final LdapTemplate ldapTemplate;

    /**
     * Fetches all users from LDAP under:
     *   ou=users
     *
     * Filter:
     *   (objectClass=inetOrgPerson)
     *
     * This assumes LDAP structure follows standard inetOrgPerson schema.
     */
    public List<LdapUserDTO> fetchAllUsers() {
        return ldapTemplate.search(
                "ou=users",
                "(objectClass=inetOrgPerson)",
                this::mapToDto
        );
    }

    /**
     * Maps LDAP attributes → internal DTO.
     *
     * IMPORTANT:
     * Field mapping is tightly coupled to LDAP schema.
     * Any LDAP schema change must be reflected here.
     */
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

    /**
     * Safe attribute extractor.
     * Returns null if attribute is missing.
     */
    private String getString(Attributes attrs, String key) throws NamingException {
        Attribute attr = attrs.get(key);
        return attr != null ? attr.get().toString() : null;
    }

    /**
     * Extracts LDAP password attribute.
     *
     * NOTE:
     * LDAP passwords may be:
     * - hashed (byte[])
     * - encoded string
     *
     * Current implementation preserves raw value as received.
     * Password handling strategy should be reviewed for production LDAP.
     */
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