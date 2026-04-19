package de.bdr.asset.management.core.config.security.ldap;

import de.bdr.asset.management.user.User;
import de.bdr.asset.management.user.UserRepository;
import de.bdr.asset.management.user.UserRoleEnum;
import de.bdr.asset.management.user.UserStatusEnum;
import de.bdr.asset.management.user.department.Department;
import de.bdr.asset.management.user.department.DepartmentEnum;
import de.bdr.asset.management.user.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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

        // preload users to avoid N+1
        Map<String, User> usersByUsername = userRepository.findAll()
                .stream()
                .collect(Collectors.toMap(User::getUsername, u -> u));

        // preload departments
        Map<DepartmentEnum, Department> departments = departmentRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Department::getName, d -> d));

        // =========================
        // PHASE 1 — create/update
        // =========================
        for (LdapUserDTO ldapUser : ldapUsers) {

            User existing = usersByUsername.get(ldapUser.username());

            if (existing == null) {
                User created = createUser(ldapUser, departments);
                usersByUsername.put(created.getUsername(), created);
                log.info("Created user: {}", created.getUsername());
            } else {
                boolean updated = updateUser(existing, ldapUser, departments);
                if (updated) {
                    log.info("Updated user: {}", existing.getUsername());
                }
            }
        }

        // =========================
        // PHASE 2 — manager resolve
        // =========================
        for (LdapUserDTO ldapUser : ldapUsers) {

            User user = usersByUsername.get(ldapUser.username());
            if (user == null) continue;

            String newManagerEmail = resolveManagerEmail(
                    ldapUser.managerDn(),
                    usersByUsername
            );

            if (!Objects.equals(user.getManagerEmail(), newManagerEmail)) {
                user.setManagerEmail(newManagerEmail);
            }
        }
    }

    private User createUser(LdapUserDTO ldapUser,
                            Map<DepartmentEnum, Department> departments) {

        User user = new User();

        user.setUsername(ldapUser.username());
        user.setName(ldapUser.name());
        user.setSurname(ldapUser.surname());
        user.setEmail(ldapUser.email());

        if (ldapUser.password() != null) {
            user.setPassword(passwordEncoder.encode(ldapUser.password()));
        }

        user.setRole(mapRole(ldapUser.employeeType()));
        user.setStatus(UserStatusEnum.ACTIVE);

        user.setDepartment(resolveDepartment(ldapUser.department(), departments));

        user.setBenefit("STANDARD");
        user.setNotes(
                ldapUser.title() != null ? ldapUser.title() : "LDAP sync"
        );

        user.setManagerEmail(null);

        return userRepository.save(user);
    }

    private boolean updateUser(User user,
                               LdapUserDTO ldapUser,
                               Map<DepartmentEnum, Department> departments) {

        boolean changed = false;

        if (!Objects.equals(user.getName(), ldapUser.name())) {
            user.setName(ldapUser.name());
            changed = true;
        }

        if (!Objects.equals(user.getSurname(), ldapUser.surname())) {
            user.setSurname(ldapUser.surname());
            changed = true;
        }

        if (!Objects.equals(user.getEmail(), ldapUser.email())) {
            user.setEmail(ldapUser.email());
            changed = true;
        }

        UserRoleEnum newRole = mapRole(ldapUser.employeeType());
        if (!Objects.equals(user.getRole(), newRole)) {
            user.setRole(newRole);
            changed = true;
        }

        if (ldapUser.department() != null) {
            Department newDept = resolveDepartment(ldapUser.department(), departments);

            if (user.getDepartment() == null ||
                    !Objects.equals(user.getDepartment().getId(), newDept.getId())) {

                user.setDepartment(newDept);
                changed = true;
            }
        }

        // password only if missing
        if (user.getPassword() == null && ldapUser.password() != null) {
            user.setPassword(passwordEncoder.encode(ldapUser.password()));
            changed = true;
        }

        if (user.getNotes() == null) {
            user.setNotes("LDAP sync");
            changed = true;
        }

        if (user.getBenefit() == null) {
            user.setBenefit("STANDARD");
            changed = true;
        }

        if (changed) {
            userRepository.save(user);
        }

        return changed;
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
    private Department resolveDepartment(String dept,
                                         Map<DepartmentEnum, Department> departments) {

        if (dept == null) {
            return getDefaultDepartment(departments);
        }

        try {
            DepartmentEnum enumVal = DepartmentEnum.valueOf(dept);

            Department department = departments.get(enumVal);

            if (department == null) {
                throw new IllegalStateException("Department not found: " + enumVal);
            }

            return department;

        } catch (Exception e) {
            return getDefaultDepartment(departments);
        }
    }
    
    private Department getDefaultDepartment(Map<DepartmentEnum, Department> departments) {
        return departments.values()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No department found"));
    }

    private String resolveManagerEmail(String managerDn,
                                       Map<String, User> users) {

        if (managerDn == null) return null;

        String uid = extractUid(managerDn);
        if (uid == null) return null;

        return Optional.ofNullable(users.get(uid))
                .map(User::getEmail)
                .orElse(null);
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