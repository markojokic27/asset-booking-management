package de.bdr.asset.management.assetcategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * AssetCategory Service
 */
public interface AssetCategoryService {

    /** CREATE */
    AssetCategoryResponseDTO createAssetCategory(AssetCategoryRequestDTO assetCategoryRequest);

    /** READ */
    AssetCategoryResponseDTO getAssetCategoryById(Long id);
    Page<AssetCategoryResponseDTO> getAllAssetCategories(Pageable pageable);

    /** UPDATE */
    AssetCategoryResponseDTO updateAssetCategory(Long id, AssetCategoryRequestDTO assetCategoryRequest);

    /** DELETE (Soft) */
    void deleteAssetCategory(Long id);
}
