package de.bdr.asset.management.core.ldap;

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

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LdapSyncService {

    /**
     * Service responsible for fetching users from LDAP directory.
     */
    private final LdapService ldapService;

    /**
     * Repository for application users.
     */
    private final UserRepository userRepository;

    /**
     * Repository for departments (used to resolve FK relationships).
     */
    private final DepartmentRepository departmentRepository;

    /**
     * Password encoder.
     * NOTE: Currently used even though passwords come from LDAP.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Main synchronization method.
     *
     * Synchronizes users from LDAP into the local database.
     *
     * FLOW:
     * 1. Fetch users from LDAP
     * 2. Load existing DB users (only relevant ones)
     * 3. Load departments
     * 4. Phase 1: Create or update users
     * 5. Phase 2: Resolve manager relationships
     *
     * NOTE:
     * - Runs in a single transaction for simplicity
     * - Safe for small datasets (~150 users)
     * - Can be split into multiple transactions in future if needed
     */
    @Transactional
    public void syncUsers() {

        // Fetch all users from LDAP
        List<LdapUserDTO> ldapUsers = ldapService.fetchAllUsers();

        // Extract usernames from LDAP users
        // Used to load only relevant DB users (avoids full table scan)
        List<String> usernames = ldapUsers.stream()
                .map(LdapUserDTO::username)
                .toList();

        // Load existing users from DB and map by username for O(1) access
        Map<String, User> usersByUsername = userRepository
                .findByUsernameIn(usernames)
                .stream()
                .collect(Collectors.toMap(User::getUsername, u -> u));

        // Preload all departments into memory
        // Keyed by enum for fast lookup during sync
        Map<DepartmentEnum, Department> departments = departmentRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Department::getName, d -> d));

        // =========================
        // PHASE 1 — create/update
        // =========================
        // Goal:
        // - Create users that don't exist
        // - Update existing users if any relevant field changed
        for (LdapUserDTO ldapUser : ldapUsers) {

            User existing = usersByUsername.get(ldapUser.username());

            if (existing == null) {
                // New user → create
                User created = createUser(ldapUser, departments);

                // Add to map so it's available for manager resolution later
                usersByUsername.put(created.getUsername(), created);

                log.info("Created user: {}", created.getUsername());
            } else {
                // Existing user → update only if needed
                boolean updated = updateUser(existing, ldapUser, departments);

                if (updated) {
                    log.info("Updated user: {}", existing.getUsername());
                }
            }
        }

        // =========================
        // PHASE 2 — manager resolve
        // =========================
        // Goal:
        // - Set managerEmail based on LDAP "manager" DN
        // - Done AFTER phase 1 to ensure all users exist in DB/map
        for (LdapUserDTO ldapUser : ldapUsers) {

            User user = usersByUsername.get(ldapUser.username());
            if (user == null) continue;

            String newManagerEmail = resolveManagerEmail(
                    ldapUser.managerDn(),
                    usersByUsername
            );

            // Update only if changed to avoid unnecessary DB writes
            if (!Objects.equals(user.getManagerEmail(), newManagerEmail)) {
                user.setManagerEmail(newManagerEmail);
                userRepository.save(user);
            }
        }

        /*
         * =========================
         * PHASE 3 — detect removed users
         * =========================
         *
         * PURPOSE:
         * Detect users that exist in DB but are no longer present in LDAP.
         *
         * if (ldapUsers.isEmpty()) {
         *     log.warn("LDAP returned 0 users — skipping deletion phase");
         *     return;
         * }
         *
         * List<User> allDbUsers = userRepository.findAll();
         *
         * Set<String> ldapUsernames = ldapUsers.stream()
         *         .map(LdapUserDTO::username)
         *         .collect(Collectors.toSet());
         *
         * List<User> missingInLdap = allDbUsers.stream()
         *         .filter(user -> !ldapUsernames.contains(user.getUsername()))
         *         .toList();
         *
         * for (User user : missingInLdap) {
         *     user.setStatus(UserStatusEnum.DELETED);
         *     userRepository.save(user);
         *
         *     log.info("Deactivated user (missing from LDAP): {}", user.getUsername());
         * }
         */
    }

    /**
     * Creates a new User entity from LDAP data
     */
    private User createUser(LdapUserDTO ldapUser,
                            Map<DepartmentEnum, Department> departments) {

        User user = new User();

        user.setUsername(ldapUser.username());
        user.setName(ldapUser.name());
        user.setSurname(ldapUser.surname());
        user.setEmail(ldapUser.email());

        // TODO:
        // Currently encoding LDAP password.
        // This may NOT be correct if LDAP already stores hashed passwords.
        // Future: revisit password strategy.
        if (ldapUser.password() != null) {
            user.setPassword(passwordEncoder.encode(ldapUser.password()));
        }

        user.setRole(mapRole(ldapUser.employeeType()));
        user.setStatus(UserStatusEnum.ACTIVE);

        user.setDepartment(resolveDepartment(ldapUser.department(), departments));

        // Default values for internal system
        user.setBenefit("STANDARD");
        user.setNotes(
                ldapUser.title() != null ? ldapUser.title() : "LDAP sync"
        );

        // Manager resolved later in phase 2
        user.setManagerEmail(null);

        return userRepository.save(user);
    }

    /**
     * Updates an existing user if any relevant field has changed.
     *
     * Returns true if any update occurred.
     */
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

        // Department update
        if (ldapUser.department() != null) {
            Department newDept = resolveDepartment(ldapUser.department(), departments);

            if (user.getDepartment() == null ||
                    !Objects.equals(user.getDepartment().getId(), newDept.getId())) {

                user.setDepartment(newDept);
                changed = true;
            }
        }

        // TODO:
        // Only set password if missing
        // (prevents overwriting existing password repeatedly)
        if (user.getPassword() == null && ldapUser.password() != null) {
            user.setPassword(passwordEncoder.encode(ldapUser.password()));
            changed = true;
        }

        // Ensure defaults exist
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

    /**
     * Maps LDAP employeeType → internal role.
     *
     * Case-insensitive to tolerate LDAP inconsistencies.
     */
    private UserRoleEnum mapRole(String employeeType) {
        if (employeeType == null) return UserRoleEnum.EMPLOYEE;

        return Arrays.stream(UserRoleEnum.values())
                .filter(role -> role.name().equalsIgnoreCase(employeeType))
                .findFirst()
                .orElse(UserRoleEnum.EMPLOYEE);
    }

    /**
     * Resolves department from LDAP value.
     *
     * NOTE:
     * - Uses enum mapping
     * - Falls back to "default" department if unknown
     *
     * TODO
     * WARNING:
     * Current fallback uses first available department
     */
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

    /**
     * Returns fallback department.
     *
     * TODO
     * WARNING:
     * Currently returns first department from DB.
     * This is a temporary solution and should be replaced.
     */
    private Department getDefaultDepartment(Map<DepartmentEnum, Department> departments) {
        return departments.values()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No department found"));
    }

    /**
     * Resolves manager email from LDAP manager DN.
     *
     * Steps:
     * - Extract UID from DN
     * - Find user in map
     * - Return their email
     */
    private String resolveManagerEmail(String managerDn,
                                       Map<String, User> users) {

        if (managerDn == null) return null;

        String uid = extractUid(managerDn);
        if (uid == null) return null;

        return Optional.ofNullable(users.get(uid))
                .map(User::getEmail)
                .orElse(null);
    }

    /**
     * Extracts UID from LDAP DN using proper parsing.
     *
     * Example DN:
     * uid=john,ou=users,dc=myapp,dc=com
     *
     * Uses LdapName to correctly handle:
     * - escaped characters
     * - different DN structures
     */
    private String extractUid(String dn) {
        if (dn == null) return null;

        try {
            LdapName ldapName = new LdapName(dn);

            for (Rdn rdn : ldapName.getRdns()) {
                if ("uid".equalsIgnoreCase(rdn.getType())) {
                    return rdn.getValue().toString();
                }
            }

        } catch (Exception e) {
            log.warn("Failed to parse DN: {}", dn, e);
        }

        return null;
    }

}