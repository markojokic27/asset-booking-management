package de.bdr.asset.management.asset;

import java.util.List;

public interface AssetService {

    // CREATE
    Asset createAsset(AssetDTO dto);

    // READ
    Asset getAssetById(Long id);
    List<Asset> getAllAssets();

    // UPDATE
    Asset updateAsset(Long id, AssetDTO dto);
}