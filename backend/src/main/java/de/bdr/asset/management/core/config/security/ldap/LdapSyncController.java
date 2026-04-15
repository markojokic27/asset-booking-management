package de.bdr.asset.management.core.config.security.ldap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ldap")
public class LdapSyncController {

    private final LdapSyncService ldapSyncService;

    public LdapSyncController(LdapSyncService ldapSyncService) {
        this.ldapSyncService = ldapSyncService;
    }

    @Operation(
            summary = "Sync LDAP users to database",
            description = """ 
                              Triggers synchronization of users from LDAP into the local database.
                              Creates new users and updates existing ones.
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