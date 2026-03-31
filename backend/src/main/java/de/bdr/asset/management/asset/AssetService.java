package de.bdr.asset.management.asset;

import java.util.List;

public interface AssetService {

    /** CREATE */
    AssetDTO createAsset(AssetDTO dto);

    /** READ */
    AssetDTO getAssetById(Long id);
    List<AssetDTO> getAllAssets();

    /** UPDATE */
    AssetDTO updateAsset(Long id, AssetDTO dto);
}