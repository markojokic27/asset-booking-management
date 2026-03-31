package de.bdr.asset.management.asset;

import java.util.*;

import org.springframework.stereotype.Service;

/**
 * Implementation of Asset Service
 */
@Service
public class AssetServiceImpl implements AssetService {
    // TODO: Update the functions to not use dummy data
    private final AssetRepository repository;

    public AssetServiceImpl(AssetRepository repository) {
        this.repository = repository;
    }

    /**
     * Create asset in DB.
     *
     * @param assetRequest - an AssetDTO record
     * @return an Asset record
     */
    @Override
    public AssetDTO createAsset(AssetDTO assetRequest) {
        // TODO: Implement a mapper function to handle this
        
        return new AssetDTO(
            1L,
            assetRequest.name(),
            assetRequest.categoryId(),
            assetRequest.description(),
            assetRequest.code(),
            assetRequest.status(),
            assetRequest.location()
        );
    }

    /**
     * Returns a specific asset.
     *
     * @param id - a Long id
     * @return an Asset record
     */
    @Override
    public AssetDTO getAssetById(Long id) {
        return new AssetDTO(
            1L,
            "Dummy Asset Get by Id",
            1L,
            "Dummy Asset Desc",
            "Code string",
            AssetStatusEnum.ACTIVE,
            "Prizemlje"
        );
    }

    /**
     * Returns a list of assets.
     *
     * @return a list of Asset records
     */
    @Override
    public List<AssetDTO> getAllAssets() {
        List<AssetDTO> dummyList = new ArrayList<>();
        dummyList.add(
            new AssetDTO(
                1L,
                "Dummy Asset 1",
                1L,
                "Dummy Asset Desc",
                "Code string",
                AssetStatusEnum.ACTIVE,
                "Prizemlje"
            )
        );

        dummyList.add(
            new AssetDTO(
                2L,
                "Dummy Asset 2",
                2L,
                "Dummy Asset Desc",
                "Code string",
                AssetStatusEnum.DAMAGED,
                "Kat"
            )
        );

        return dummyList;
    }

    /**
     * Update and return a specific asset.
     *
     * @param id - a Long id
     * @param assetRequest - an Asset record
     * @return an Asset record
     */
    @Override
    public AssetDTO updateAsset(Long id, AssetDTO assetRequest) {
        return new AssetDTO(
            1L,
            "Dummy Asset Update",
            1L,
            "Dummy Asset Desc",
            "Code string",
            AssetStatusEnum.ACTIVE,
            "Prizemlje"
        );
    }
}