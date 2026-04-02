package de.bdr.asset.management.asset;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Asset Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    /** CREATE */
    @PostMapping
    public ResponseEntity<AssetResponseDTO> createAsset(@Valid @RequestBody AssetRequestDTO assetRequest) {
        log.info("Received POST request to create a new asset");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assetService.createAsset(assetRequest));
    }

    /** READ ALL */
    @GetMapping
    public ResponseEntity<List<AssetResponseDTO>> getAllAssets() {
        log.info("Received GET request to fetch all assets");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(assetService.getAllAssets());
    }

    /** READ BY ID */
    @GetMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> getAssetById(@PathVariable Long id) {
        log.info("Received GET request to fetch asset with id: {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(assetService.getAssetById(id));
    }

    /** UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> updateAsset(@PathVariable Long id, @Valid @RequestBody AssetRequestDTO assetRequest) {
        log.info("Received PUT request to update asset with id: {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(assetService.updateAsset(id, assetRequest));
    }
}