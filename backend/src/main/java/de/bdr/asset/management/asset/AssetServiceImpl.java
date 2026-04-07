package de.bdr.asset.management.asset;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.assetcategory.AssetCategoryRepository;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;

/**
 * Implementation of Asset Service
 */
@Slf4j
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
        log.info("Attempting to create asset in category id: {}", assetRequest.categoryId());

        AssetCategory category = assetCategoryRepository.findById(assetRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("AssetCategory does not exist for id: " + assetRequest.categoryId()));

        log.debug("Asset category found. Mapping entity and saving to database...");


        Asset asset = mapper.toEntity(assetRequest);
        asset.setCategory(category);
        asset = repository.save(asset);

        log.info("Successfully created new asset with id: {} in asset category id: {}", asset.getId(), category.getId());

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

        log.info("Asset found with id: {}", id);

        return mapper.toResponse(asset);
    }

    /**
     * Returns a page of assets.
     *
     * @param pageable - A Pageable object, determines the page, size and sort
     * @return a page of Asset records
     */
    @Override
    public Page<AssetResponseDTO> getAllAssets(Pageable pageable) {
        log.debug("Fetching assets from the database with pagination: " +
                        "Page number: {} | Page size: {} | Sort: {}",
                        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()
        );

        Page<Asset> assets = repository.findAll(pageable);

        log.info("Successfully fetched {} assets", assets.getNumberOfElements());

        return assets.map(mapper::toResponse);
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
        log.info("Attempting to update asset with id: {}", id);

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

        log.info("Successfully updated asset with id: {}", id);
        
        return mapper.toResponse(asset);
    }

}