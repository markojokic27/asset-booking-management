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

import java.util.List;

@Service
@RequiredArgsConstructor
public class LdapSyncService {

    private final LdapService ldapService;
    private final UserRepository userRepository;
    // private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public void syncUsers() {

        List<LdapUserDTO> ldapUsers = ldapService.fetchAllUsers();

        for (LdapUserDTO ldapUser : ldapUsers) {

            // Prevent duplicates
            if (userRepository.existsByUsername(ldapUser.username())) {
                throw new IllegalStateException("User already exists");
            }

            // Save user in DB
            // TODO
            User user = User.builder()
                    .username(ldapUser.username())
                    .name(ldapUser.name())
                    .surname(ldapUser.surname())
                    .email(ldapUser.email())
                    .password(passwordEncoder.encode(ldapUser.password()))
                    .role(UserRoleEnum.EMPLOYEE)
                    .status(UserStatusEnum.ACTIVE)
                    .department(Department.builder()
                            .id(1L)
                            .name(DepartmentEnum.DEVOPS)
                            .build())
                    .benefit("STANDARD")
                    .managerEmail("manager@company.com")
                    .notes("Created from LDAP")
                    .build();

            userRepository.save(user);
        }
    }

}