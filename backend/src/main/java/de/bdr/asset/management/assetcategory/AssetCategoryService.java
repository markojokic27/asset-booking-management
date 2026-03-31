package de.bdr.asset.management.assetcategory;

import java.util.List;

/**
 * AssetCategory Service
 */
public interface AssetCategoryService {

    /** CREATE */
    AssetCategoryRequestDTO createAssetCategory(AssetCategoryRequestDTO assetCategoryRequest);

    /** READ */
    AssetCategoryRequestDTO getAssetCategoryById(Long id);
    List<AssetCategoryRequestDTO> getAllAssetCategories();

    /** UPDATE */
    AssetCategoryRequestDTO updateAssetCategory(Long id, AssetCategoryRequestDTO assetCategoryRequest);

    /** DELETE (Soft) */
    void deleteAssetCategory(Long id);
}
