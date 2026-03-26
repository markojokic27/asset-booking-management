package de.bdr.asset.management.assetcategory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/asset-categories")
public class AssetCategoryController {
    private final AssetCategoryService service;

    public AssetCategoryController(AssetCategoryService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public AssetCategoryDTO create(@RequestBody AssetCategoryDTO request) {
        // TODO: Explore RequestEntity for returning responses
        return service.createAssetCategory(request);
    }
    // READ
    // ALL
    @GetMapping
    public List<AssetCategoryDTO> getAll() {
        // TODO: Explore RequestEntity for returning responses
        return service.getAllAssetCategories();
    }

    // BY ID
    @GetMapping("/{id}")
    public AssetCategoryDTO getById(@PathVariable Long id) {
        // TODO: Explore RequestEntity for returning responses
        return service.getAssetCategoryById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public AssetCategoryDTO update(@PathVariable Long id, @RequestBody AssetCategoryDTO request) {
        // TODO: Explore RequestEntity for returning responses
        return service.updateAssetCategory(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // TODO: Explore RequestEntity for returning responses
        service.deleteAssetCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
