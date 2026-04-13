package de.bdr.asset.management.core.config.security.ldap;

import lombok.RequiredArgsConstructor;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LdapUserService {

    private final LdapTemplate ldapTemplate;

    public Optional<CustomLdapUserDetails> findByUid(String uid) {

        List<CustomLdapUserDetails> results = ldapTemplate.search(
                LdapQueryBuilder.query()
                        .base("ou=users")
                        .where("uid").is(uid),
                (AttributesMapper<CustomLdapUserDetails>) attrs -> {

                    String email = getAttr(attrs, "mail");
                    String displayName = getAttr(attrs, "cn");
                    String department = getAttr(attrs, "departmentNumber");

                    return CustomLdapUserDetails.builder()
                            .username(getAttr(attrs, "uid"))
                            .password("") // LDAP bind already validated password
                            .email(email)
                            .displayName(displayName)
                            .department(department)
                            .build();
                }
        );

        return results.stream().findFirst();
    }

    private String getAttr(Attributes attrs, String name) throws javax.naming.NamingException {
        Attribute attr = attrs.get(name);
        return attr != null ? (String) attr.get() : null;
    }
}