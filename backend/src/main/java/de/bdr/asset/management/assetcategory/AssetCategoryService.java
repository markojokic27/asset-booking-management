package de.bdr.asset.management.assetcategory;

import java.util.List;

/**
 * AssetCategory Service
 */
public interface AssetCategoryService {

    /** CREATE */
    AssetCategoryResponseDTO createAssetCategory(AssetCategoryRequestDTO assetCategoryRequest);

    /** READ */
    AssetCategoryResponseDTO getAssetCategoryById(Long id);
    List<AssetCategoryResponseDTO> getAllAssetCategories();

    /** UPDATE */
    AssetCategoryResponseDTO updateAssetCategory(Long id, AssetCategoryRequestDTO assetCategoryRequest);

    /** DELETE (Soft) */
    void deleteAssetCategory(Long id);
}
