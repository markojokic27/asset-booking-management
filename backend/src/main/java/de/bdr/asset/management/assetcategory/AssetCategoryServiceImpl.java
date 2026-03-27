package de.bdr.asset.management.assetcategory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
/**
 * Implementation of AssetCategory Service
 * Currently returns only dummy data.
 */
@Service
public class AssetCategoryServiceImpl implements AssetCategoryService {
    // TODO: Update the functions to not use dummy data
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
        return new AssetCategoryDTO(
                1L,
                "Dummy Asset category",
                "Dummy Desc",
                BookingPeriodEnum.HOUR,
                false
        );
    }

    /**
     * Returns a specific asset category.
     *
     * @param id - a Long id
     * @return an AssetCategoryResponseDTO record
     */
    @Override
    public AssetCategoryDTO getAssetCategoryById(Long id){
        return new AssetCategoryDTO(
                1L,
                "Dummy Asset category",
                "Dummy Desc",
                BookingPeriodEnum.HOUR,
                false
        );
    }

    /**
     * Returns a list of asset categories.
     *
     * @return a list of AssetCategoryResponseDTO records
     */
    @Override
    public List<AssetCategoryDTO> getAllAssetCategories(){
        List<AssetCategoryDTO> dummyList = new ArrayList<>();
        dummyList.add(
            new AssetCategoryDTO(
                    1L,
                    "Dummy Asset category",
                    "Dummy Desc",
                    BookingPeriodEnum.HOUR,
                    false
            )
        );
        dummyList.add(
                new AssetCategoryDTO(
                        2L,
                        "2 Dummy Asset category",
                        "Dummy Desc 2",
                        BookingPeriodEnum.HOUR,
                        false
                )
        );
        return dummyList;
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
        return new AssetCategoryDTO(
                1L,
                "Dummy Asset category",
                "Dummy Desc",
                BookingPeriodEnum.HOUR,
                false
        );
    }

    /**
     * Delete a specific asset category.
     *
     * @param id - a Long id
     * @implNote Should be a soft delete by setting it to inactive or such
     */
    @Override
    public void deleteAssetCategoryById(Long id){

    }
}
