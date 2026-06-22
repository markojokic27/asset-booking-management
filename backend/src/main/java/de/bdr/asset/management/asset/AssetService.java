package de.bdr.asset.management.asset;

import de.bdr.asset.management.asset.dtos.AssetRequestDTO;
import de.bdr.asset.management.asset.dtos.AssetResponseDTO;
import de.bdr.asset.management.asset.dtos.AssetUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetService {

    /** CREATE */
    AssetResponseDTO createAsset(AssetRequestDTO dto);

    /** READ */
    AssetResponseDTO getAssetById(Long id);
    Page<AssetResponseDTO> getAllAssets(AssetFilter filter, Pageable pageable);

    /** UPDATE */
    AssetResponseDTO updateAsset(Long id, AssetUpdateRequestDTO dto);
    AssetResponseDTO updateAssetQRCode(Long id, String filePath);

    /** DELETE */
    void softDeleteAsset(Long id);

    Asset getActiveAssetById(Long id);
}