package de.bdr.asset.management.assetcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

/**
 * Asset Category Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/asset-categories")
public class AssetCategoryController {
    private final AssetCategoryService service;

    public AssetCategoryController(AssetCategoryService service) {
        this.service = service;
    }

    /** CREATE */
    @PostMapping
    public ResponseEntity<AssetCategoryResponseDTO> create(@Valid @RequestBody AssetCategoryRequestDTO request) {
        log.info("Received POST request to create a new asset category");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createAssetCategory(request));
    }

    /** READ ALL */
    @GetMapping
    public ResponseEntity<Page<AssetCategoryResponseDTO>> getAll(
            Pageable pageable
    ) {
        log.info("Received GET request to fetch all asset categories");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllAssetCategories(pageable));
    }

    /** READ BY ID */
    @GetMapping("/{id}")
    ResponseEntity<AssetCategoryResponseDTO> getById(@PathVariable Long id) {
        log.info("Received GET request to fetch asset category with id: {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAssetCategoryById(id));
    }

    /** UPDATE */
    @PutMapping("/{id}")
    ResponseEntity<AssetCategoryResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AssetCategoryRequestDTO request) {
        log.info("Received PUT request to update asset category with id: {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateAssetCategory(id, request));
    }

    /** Soft DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Received DELETE request for asset category with id: {}", id);

        service.deleteAssetCategory(id);

        log.debug("Successfully processed DELETE request for asset category id: {}", id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
