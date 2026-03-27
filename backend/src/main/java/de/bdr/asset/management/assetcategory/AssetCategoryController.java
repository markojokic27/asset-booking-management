package de.bdr.asset.management.assetcategory;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/asset-categories")
public class AssetCategoryController {
    private final AssetCategoryService service;

    public AssetCategoryController(AssetCategoryService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<AssetCategoryDTO> create(@Valid @RequestBody AssetCategoryDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createAssetCategory(request));
    }
    // READ
    // ALL
    @GetMapping
    public ResponseEntity<List<AssetCategoryDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllAssetCategories());
    }

    // BY ID
    @GetMapping("/{id}")
    ResponseEntity<AssetCategoryDTO> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAssetCategoryById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    ResponseEntity<AssetCategoryDTO> update(@PathVariable Long id, @Valid @RequestBody AssetCategoryDTO request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateAssetCategory(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteAssetCategory(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
