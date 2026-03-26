package de.bdr.asset.management.asset;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    //READ ALL
    @GetMapping
    public List<AssetDTO> getAllAssets() {
        return assetService.getAllAssets();
    }

    // BY ID
    @GetMapping("/{id}")
    public AssetDTO getAssetById(@PathVariable Long id) {
        return assetService.getAssetById(id);
    }

    // CREATE
    @PostMapping
    public AssetDTO createAsset(@Valid @RequestBody AssetDTO dto) {
        return assetService.createAsset(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public AssetDTO updateAsset(@PathVariable Long id, @Valid @RequestBody AssetDTO dto) {
        return assetService.updateAsset(id, dto);
    }
}