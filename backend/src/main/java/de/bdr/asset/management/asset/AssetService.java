package de.bdr.asset.management.asset;

import java.util.List;

public interface AssetService {

    /** CREATE */
    AssetResponseDTO createAsset(AssetRequestDTO dto);

    /** READ */
    AssetResponseDTO getAssetById(Long id);
    List<AssetResponseDTO> getAllAssets();

    /** UPDATE */
    AssetResponseDTO updateAsset(Long id, AssetRequestDTO dto);
}