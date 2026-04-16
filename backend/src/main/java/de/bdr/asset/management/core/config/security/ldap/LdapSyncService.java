package de.bdr.asset.management.core.config.security.ldap;

import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;
import de.bdr.asset.management.user.department.Department;
import de.bdr.asset.management.user.department.DepartmentEnum;
import de.bdr.asset.management.user.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LdapSyncService {

    private final LdapService ldapService;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void syncUsers() {

        List<LdapUserDTO> ldapUsers = ldapService.fetchAllUsers();

        for (LdapUserDTO ldapUser : ldapUsers) {

            User user = userRepository
                    .findByUsername(ldapUser.username())
                    .orElse(new User());

            user.setUsername(ldapUser.username());
            user.setName(ldapUser.name());
            user.setSurname(ldapUser.surname());
            user.setEmail(ldapUser.email());
            //
            user.setPassword(ldapUser.password());
            user.setRole(mapRole(ldapUser.employeeType()));
            //
            user.setStatus(UserStatusEnum.ACTIVE);

            // ---------- department ----------
            user.setDepartment(resolveDepartment(ldapUser.department()));

            // ---------- manager ----------
            user.setManagerEmail(resolveManagerEmail(ldapUser.managerDn()));

            // ---------- defaults ----------
            user.setBenefit("STANDARD");
            user.setNotes(ldapUser.title() != null ? ldapUser.title() : "LDAP sync");

            userRepository.save(user);
        }
    }

    // ---------- helpers ----------

    private UserRoleEnum mapRole(String employeeType) {
        if (employeeType == null) return UserRoleEnum.EMPLOYEE;

        try {
            return UserRoleEnum.valueOf(employeeType);
        } catch (Exception e) {
            return UserRoleEnum.EMPLOYEE;
        }
    }

    private Department resolveDepartment(String dept) {
        if (dept == null) {
            return getDefaultDepartment();
        }

        try {
            DepartmentEnum enumVal = DepartmentEnum.valueOf(dept);

            return departmentRepository.findByName(enumVal)
                    .orElseGet(() -> departmentRepository.save(
                            Department.builder()
                                    .name(enumVal)
                                    .build()
                    ));

        } catch (Exception e) {
            return getDefaultDepartment();
        }
    }

    private Department getDefaultDepartment() {
        return departmentRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No department found"));
    }

    private String resolveManagerEmail(String managerDn) {
        if (managerDn == null) return "none";

        String username = extractUid(managerDn);

        return userRepository.findByUsername(username)
                .map(User::getEmail)
                .orElse("none");
    }

    private String extractUid(String dn) {
        // uid=manager,ou=users,...
        try {
            return dn.split(",")[0].split("=")[1];
        } catch (Exception e) {
            return null;
        }
    }
}