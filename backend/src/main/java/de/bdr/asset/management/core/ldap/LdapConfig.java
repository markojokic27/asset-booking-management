package de.bdr.asset.management.core.ldap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableLdapRepositories(basePackages = "com.yourapp.config.security.ldap.repository")
public class LdapConfig {

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource ctx = new LdapContextSource();
        ctx.setUrl("ldap://localhost:8389");
        ctx.setBase("dc=myapp,dc=com");
        // For anonymous bind (embedded dev server)
        // For production: set manager DN and password
        // ctx.setUserDn("cn=readonly,dc=myapp,dc=com");
        // ctx.setPassword(ldapPassword);

        ctx.afterPropertiesSet();
        return ctx;
    }

    @Bean
    public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }
}