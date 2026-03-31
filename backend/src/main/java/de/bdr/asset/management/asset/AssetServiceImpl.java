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
     * @param assetRequest - an AssetRequestDTO record
     * @return an Asset record
     */
    @Override
    public AssetRequestDTO createAsset(AssetRequestDTO assetRequest) {
        // TODO: Implement a mapper function to handle this
        
        return new AssetRequestDTO(
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
    public AssetRequestDTO getAssetById(Long id) {
        return new AssetRequestDTO(
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
    public List<AssetRequestDTO> getAllAssets() {
        List<AssetRequestDTO> dummyList = new ArrayList<>();
        dummyList.add(
            new AssetRequestDTO(
                "Dummy Asset 1",
                1L,
                "Dummy Asset Desc",
                "Code string",
                AssetStatusEnum.ACTIVE,
                "Prizemlje"
            )
        );

        dummyList.add(
            new AssetRequestDTO(
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
    public AssetRequestDTO updateAsset(Long id, AssetRequestDTO assetRequest) {
        return new AssetRequestDTO(
            "Dummy Asset Update",
            1L,
            "Dummy Asset Desc",
            "Code string",
            AssetStatusEnum.ACTIVE,
            "Prizemlje"
        );
    }
}