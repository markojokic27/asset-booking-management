package de.bdr.asset.management.asset;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of Asset Service
 */
@Service
public class AssetServiceImpl implements AssetService {

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
    public Asset createAsset(AssetDTO assetRequest) {
        // TODO: IMPLEMENT THE FUNCTION
        return null;
    }

    /**
     * Returns a specific asset.
     *
     * @param id - a Long id
     * @return an Asset record
     */
    @Override
    public Asset getAssetById(Long id) {
        // TODO: IMPLEMENT THE FUNCTION
        return null;
    }

    /**
     * Returns a list of assets.
     *
     * @return a list of Asset records
     */
    @Override
    public List<Asset> getAllAssets() {
        // TODO: IMPLEMENT THE FUNCTION
        return null;
    }

    /**
     * Update and return a specific asset.
     *
     * @param id - a Long id
     * @param assetRequest - an Asset record
     * @return an Asset record
     */
    @Override
    public Asset updateAsset(Long id, AssetDTO assetRequest) {
        // TODO: IMPLEMENT THE FUNCTION
        return null;
    }
}