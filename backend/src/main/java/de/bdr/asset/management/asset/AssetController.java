package de.bdr.asset.management.asset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import de.bdr.asset.management.asset.dtos.AssetRequestDTO;
import de.bdr.asset.management.asset.dtos.AssetResponseDTO;
import de.bdr.asset.management.asset.dtos.AssetUpdateRequestDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryResponseDTO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;

import de.bdr.asset.management.asset.qrcode.QRCodeService;
import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Asset Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/assets")
@Tag(
        name = "Assets",
        description = "Endpoints for Assets. AssetController"
)
public class AssetController {

    private final AssetService assetService;
    private final QRCodeService qrCodeService;

    public AssetController(AssetService assetService, QRCodeService qrCodeService) {
        this.assetService = assetService;
        this.qrCodeService = qrCodeService;
    }

    @Operation(
        summary = "Get asset QR Code",
        description = "If an asset does not have a file path saved, generates a new QR code, saves it to a folder and serve it. Creation is only handled the first time."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(path = "/{id}/qr-code", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getOrCreateQRCode(
            @PathVariable Long id
    ) throws WriterException, IOException, ResourceNotFoundException
    {
        File file = new File(qrCodeService.getQRCode(id));
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(file.length())
                .body(resource);
    }

    /** CREATE */
    @Operation(summary = "Create asset", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AssetResponseDTO> createAsset(
            @Valid @RequestBody AssetRequestDTO assetRequest
    ) throws ResourceNotFoundException, DuplicateResourceException
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assetService.createAsset(assetRequest));
    }

    /** READ ALL */
    @Operation(summary = "Read list of assets", description = "Only available to authenticated users. Takes a Pageable object.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<AssetResponseDTO>> getAllAssets(
            @ModelAttribute AssetFilter filter,
            @ParameterObject Pageable pageable
    ) throws IllegalArgumentException
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(assetService.getAllAssets(filter, pageable));
    }

    /** READ BY ID */
    @Operation(summary = "Read asset by ID", description = "Only available to authenticated users.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> getAssetById(
            @PathVariable Long id
    ) throws ResourceNotFoundException
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(assetService.getAssetById(id));
    }

    /** UPDATE */
    @Operation(summary = "Update asset", description = "Only available to users with role: ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> updateAsset(
            @PathVariable Long id, @Valid @RequestBody AssetUpdateRequestDTO assetRequest
    ) throws ResourceNotFoundException, DuplicateResourceException
    {
        AssetResponseDTO updatedAsset = assetService.updateAsset(id, assetRequest);
        return ResponseEntity.ok(updatedAsset);
    }

    /** Soft DELETE */
    @Operation(summary = "Delete asset", description = "Marks an asset as DELETED. Only available to ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(
            @PathVariable Long id
    ) throws ResourceNotFoundException
    {
        assetService.softDeleteAsset(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}