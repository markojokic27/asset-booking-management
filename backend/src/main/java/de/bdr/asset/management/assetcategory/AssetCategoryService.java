package de.bdr.asset.management.assetcategory;

import de.bdr.asset.management.assetcategory.dto.AssetCategoryRequestDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryResponseDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** AssetCategory Service */
public interface AssetCategoryService {

    /**
     * Creates a new asset category.
     *
     * @param assetCategoryRequest the creation data
     * @return the created category response data
     */
    AssetCategoryResponseDTO createAssetCategory(AssetCategoryRequestDTO assetCategoryRequest);

    /**
     * Retrieves an asset category by its ID.
     *
     * @param id the category ID
     * @return the found category response data
     */
    AssetCategoryResponseDTO getAssetCategoryById(Long id);

    /**
     * Retrieves a paginated list of all asset categories.
     *
     * @param pageable pagination and sorting information
     * @return a page of category response data
     */
    Page<AssetCategoryResponseDTO> getAllAssetCategories(Pageable pageable);

    /**
     * Updates an existing asset category.
     *
     * @param id the ID of the category to update
     * @param assetCategoryRequest the updated data
     * @return the updated category response data
     */
    AssetCategoryResponseDTO updateAssetCategory(Long id, AssetCategoryUpdateRequestDTO assetCategoryRequest);

    /**
     * Performs a soft delete on an asset category by its ID.
     *
     * @param id the category ID to mark as deleted
     */
    void deleteAssetCategory(Long id);
}
