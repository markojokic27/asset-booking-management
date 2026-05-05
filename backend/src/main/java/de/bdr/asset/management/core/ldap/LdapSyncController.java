package de.bdr.asset.management.core.ldap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ldap")
@RequiredArgsConstructor
public class LdapSyncController {

    private final LdapSyncService ldapSyncService;

    @Operation(
            summary = "Sync LDAP users to database",
            description = """ 
                    Triggers a full synchronization of users from LDAP into the local database.
                    
                    This includes:
                    - Creating missing users
                    - Updating existing users
                    - Resolving manager relationships
                    
                    This operation is idempotent and safe to run multiple times.   
                    """
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/sync")
    public ResponseEntity<String> sync() {
        ldapSyncService.syncUsers();
        return ResponseEntity.ok("LDAP sync completed");
    }
}