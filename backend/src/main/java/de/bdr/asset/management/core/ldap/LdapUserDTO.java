package de.bdr.asset.management.core.ldap;

public record LdapUserDTO(
        String username,
        String name,
        String surname,
        String email,
        String password,
        String department,
        String managerDn,
        String employeeType,
        String title
) {}