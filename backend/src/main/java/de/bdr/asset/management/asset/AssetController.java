package de.bdr.asset.management.asset;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }
    // CREATE
    @PostMapping
    public ResponseEntity<AssetDTO> createAsset(@Valid @RequestBody AssetDTO assetRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assetService.createAsset(assetRequest));
    }

    //READ ALL
    @GetMapping
    public ResponseEntity<List<AssetDTO>> getAllAssets() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(assetService.getAllAssets());
    }

    // BY ID
    @GetMapping("/{id}")
    public ResponseEntity<AssetDTO> getAssetById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(assetService.getAssetById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AssetDTO> updateAsset(@PathVariable Long id, @Valid @RequestBody AssetDTO assetRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(assetService.updateAsset(id, assetRequest));
    }
}