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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LdapSyncService {

    private final LdapService ldapService;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void syncUsers() {

        List<LdapUserDTO> ldapUsers = ldapService.fetchAllUsers();

        // PHASE 1 — create/update users without manager email resolution
        for (LdapUserDTO ldapUser : ldapUsers) {

            userRepository.findByUsername(ldapUser.username())
                    .ifPresentOrElse(
                            existing -> updateUser(existing, ldapUser),
                            () -> createUser(ldapUser)
                    );
        }

        // PHASE 2 - manager email resolution
        for (LdapUserDTO ldapUser : ldapUsers) {
            String managerEmail = resolveManagerEmail(ldapUser.managerDn());

            userRepository.findByUsername(ldapUser.username())
                    .ifPresent(user -> user.setManagerEmail(managerEmail));
        }

    }
    private void createUser(LdapUserDTO ldapUser) {

        User user = new User();

        user.setUsername(ldapUser.username());
        user.setName(ldapUser.name());
        user.setSurname(ldapUser.surname());
        user.setEmail(ldapUser.email());

        user.setPassword(passwordEncoder.encode(ldapUser.password()));
        user.setRole(mapRole(ldapUser.employeeType()));
        user.setStatus(UserStatusEnum.ACTIVE);

        user.setDepartment(resolveDepartment(ldapUser.department()));

        user.setBenefit("STANDARD");
        user.setNotes(ldapUser.title() != null ? ldapUser.title() : "LDAP sync");

        // This is set in the second phase
        user.setManagerEmail(null);

        userRepository.save(user);
    }

    private void updateUser(User user, LdapUserDTO ldapUser) {


        if (!Objects.equals(user.getName(), ldapUser.name())) {
            user.setName(ldapUser.name());
        }

        if (!Objects.equals(user.getSurname(), ldapUser.surname())) {
            user.setSurname(ldapUser.surname());
        }

        if (!Objects.equals(user.getEmail(), ldapUser.email())) {
            user.setEmail(ldapUser.email());
        }

        UserRoleEnum newRole = mapRole(ldapUser.employeeType());
        if (!Objects.equals(user.getRole(), newRole)) {
            user.setRole(newRole);
        }

        if (ldapUser.department() != null) {
            Department newDept = resolveDepartment(ldapUser.department());

            if (!Objects.equals(user.getDepartment().getId(), newDept.getId())) {
                user.setDepartment(newDept);
            }
        }

        if (user.getPassword() == null) {
            user.setPassword(passwordEncoder.encode(ldapUser.password()));
        }

        if (user.getNotes() == null) {
            user.setNotes("LDAP sync");
        }

        if (user.getBenefit() == null) {
            user.setBenefit("STANDARD");
        }

        userRepository.save(user);
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

        String uid = extractUid(managerDn);
        if (uid == null) return "none";

        return userRepository.findByUsername(uid)
                .map(User::getEmail)
                .orElse("none");
    }

    private String extractUid(String dn) {
        if (dn == null) return null;

        for (String part : dn.split(",")) {
            if (part.startsWith("uid=")) {
                return part.substring(4);
            }
        }

        return null;
    }
}