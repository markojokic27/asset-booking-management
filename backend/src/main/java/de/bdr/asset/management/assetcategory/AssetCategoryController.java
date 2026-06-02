package de.bdr.asset.management.assetcategory;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.bdr.asset.management.assetcategory.dto.AssetCategoryRequestDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryResponseDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryUpdateRequestDTO;
import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Asset Category Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/asset-categories")
@Tag(
        name = "Asset Categories",
        description = "Endpoints for Asset Categories. AssetCategoryController"
)
public class AssetCategoryController {
    private final AssetCategoryService service;

    public AssetCategoryController(AssetCategoryService service) {
        this.service = service;
    }

    /** CREATE */
    @Operation(summary = "Create asset category", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AssetCategoryResponseDTO> create(
            @Valid @RequestBody AssetCategoryRequestDTO request
    ) throws DuplicateResourceException
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createAssetCategory(request));
    }

    /** READ ALL */
    @Operation(summary = "Read list of asset categories", description = "Only available to authenticated users. Takes a Pageable object.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<AssetCategoryResponseDTO>> getAll(
            @ParameterObject Pageable pageable
    ) throws IllegalArgumentException
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllAssetCategories(pageable));
    }

    /** READ BY ID */
    @Operation(summary = "Read asset category by ID", description = "Only available to authenticated users.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    ResponseEntity<AssetCategoryResponseDTO> getById(
            @PathVariable Long id
    ) throws ResourceNotFoundException
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAssetCategoryById(id));
    }

    /** UPDATE */
    @Operation(summary = "Update asset category details", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    ResponseEntity<AssetCategoryResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody AssetCategoryUpdateRequestDTO request
    ) throws ResourceNotFoundException, DuplicateResourceException
    {
        AssetCategoryResponseDTO updatedCategory = service.updateAssetCategory(id, request);
        return ResponseEntity.ok(updatedCategory);
    }

    /** DELETE if no assets and bookings for category */
    @Operation(summary = "Soft delete asset category", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    )
    {
        service.deleteAssetCategory(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
