package de.bdr.asset.management.asset;

import java.util.List;

public interface AssetService {

    /** CREATE */
    AssetRequestDTO createAsset(AssetRequestDTO dto);

    /** READ */
    AssetRequestDTO getAssetById(Long id);
    List<AssetRequestDTO> getAllAssets();

    /** UPDATE */
    AssetRequestDTO updateAsset(Long id, AssetRequestDTO dto);
}