package de.bdr.asset.management.assetcategory;

import java.util.List;

public interface AssetCategoryService {
    // CREATE
    AssetCategoryDTO createAssetCategory(AssetCategoryDTO assetCategoryRequest);

    // READ
    AssetCategoryDTO getAssetCategoryById(Long id);
    List<AssetCategoryDTO> getAllAssetCategories();

    // UPDATE
    AssetCategoryDTO updateAssetCategory(Long id, AssetCategoryDTO assetCategoryRequest);

    // DELETE (SOFT)
    void deleteAssetCategory(Long id);
}
