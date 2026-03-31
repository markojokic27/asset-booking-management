package de.bdr.asset.management.asset;

import java.util.*;

import org.springframework.stereotype.Service;

import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.assetcategory.AssetCategoryRepository;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;

/**
 * Implementation of Asset Service
 */
@Service
public class AssetServiceImpl implements AssetService {
    private final AssetRepository repository;
    private final AssetMapper mapper;
    private final AssetCategoryRepository assetCategoryRepository;

    public AssetServiceImpl(AssetRepository repository, AssetMapper mapper, AssetCategoryRepository assetCategoryRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.assetCategoryRepository = assetCategoryRepository;
    }

    /**
     * Create asset in DB.
     *
     * @param assetRequest - an AssetRequestDTO record
     * @return an Asset record
     */
    @Override
    public AssetResponseDTO createAsset(AssetRequestDTO assetRequest) {
        AssetCategory category = assetCategoryRepository.findById(assetRequest.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("AssetCategory does not exist for id: " + assetRequest.categoryId()));

        Asset asset = mapper.toEntity(assetRequest);
        asset.setCategory(category);
        asset = repository.save(asset);
        
        return mapper.toResponse(asset);
    }

    /**
     * Returns a specific asset.
     *
     * @param id - a Long id
     * @return an Asset record
     */
    @Override
    public AssetResponseDTO getAssetById(Long id) {
        Asset asset = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        return mapper.toResponse(asset);
    }

    /**
     * Returns a list of assets.
     *
     * @return a list of Asset records
     */
    @Override
    public List<AssetResponseDTO> getAllAssets() {
        List<Asset> assets = repository.findAll();

        return assets.stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Update and return a specific asset.
     *
     * @param id - a Long id
     * @param assetRequest - an Asset record
     * @return an Asset record
     */
    @Override
    public AssetResponseDTO updateAsset(Long id, AssetRequestDTO assetRequest) {
        Asset asset = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        AssetCategory category = assetCategoryRepository.findById(assetRequest.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("AssetCategory does not exist for id: " + assetRequest.categoryId()));

        asset.setName(assetRequest.name());
        asset.setDescription(assetRequest.description());
        asset.setCode(assetRequest.code());
        asset.setStatus(assetRequest.status());
        asset.setCategory(category);
        asset = repository.save(asset);
        
        return mapper.toResponse(asset);
    }
}