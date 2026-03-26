package de.bdr.asset.management.assetcategory;

import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Implementation of AssetCategory Service
 */
@Service
public class AssetCategoryServiceImpl implements AssetCategoryService {
    private final AssetCategoryRepository repository;

    public AssetCategoryServiceImpl(AssetCategoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Create asset category in DB.
     *
     * @param assetCategoryRequest - a AssetCategoryDTO record
     * @return an AssetCategoryResponseDTO record
     */
    @Override
    public AssetCategoryDTO createAssetCategory(AssetCategoryDTO assetCategoryRequest){
        // TODO: IMPLEMENT THE FUNCTIONS
        return null;
    }

    /**
     * Returns a specific asset category.
     *
     * @param id - a Long id
     * @return an AssetCategoryResponseDTO record
     */
    @Override
    public AssetCategoryDTO getAssetCategoryById(Long id){
        // TODO: IMPLEMENT THE FUNCTIONS
        return null;
    }

    /**
     * Returns a list of asset categories.
     *
     * @return a list of AssetCategoryResponseDTO records
     */
    @Override
    public List<AssetCategoryDTO> getAllAssetCategories(){
        // TODO: IMPLEMENT THE FUNCTIONS
        return null;
    }

    /**
     * Update and return a specific asset category.
     *
     * @param id - a Long id
     * @param assetCategoryRequest - an AssetCategoryDTO record
     * @return an AssetCategoryResponseDTO record
     */
    @Override
    public AssetCategoryDTO updateAssetCategory(Long id, AssetCategoryDTO assetCategoryRequest){
        // TODO: IMPLEMENT THE FUNCTIONS
        return null;
    }

    /**
     * Delete a specific asset category.
     *
     * @param id - a Long id
     * @implNote Should be a soft delete by setting it to inactive or such
     */
    @Override
    public void deleteAssetCategoryById(Long id){
        // TODO: IMPLEMENT THE FUNCTIONS
    }
}
