package de.bdr.asset.management.assetcategory;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Asset Category Controller
 */
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
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createAssetCategory(request));
    }

    /** READ ALL */
    @GetMapping
    public ResponseEntity<List<AssetCategoryResponseDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllAssetCategories());
    }

    /** READ BY ID */
    @GetMapping("/{id}")
    ResponseEntity<AssetCategoryResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAssetCategoryById(id));
    }

    /** UPDATE */
    @PutMapping("/{id}")
    ResponseEntity<AssetCategoryResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AssetCategoryRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateAssetCategory(id, request));
    }

    /** Soft DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteAssetCategory(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
