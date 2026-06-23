package de.bdr.asset.management.asset;

import de.bdr.asset.management.asset.dtos.AssetRequestDTO;
import de.bdr.asset.management.asset.dtos.AssetResponseDTO;
import de.bdr.asset.management.asset.dtos.AssetUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.assetcategory.AssetCategoryRepository;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of Asset Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssetServiceImpl implements AssetService {

    public static final String ASSET_NOT_FOUND_WITH_ID = "Asset not found with id: ";

    private final AssetRepository repository;
    private final AssetMapper mapper;
    private final AssetCategoryRepository assetCategoryRepository;

    /**
     * Create asset in DB.
     *
     * @param assetRequest - an AssetRequestDTO record
     * @return an Asset record
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
                .orElseThrow(() -> new ResourceNotFoundException(ASSET_NOT_FOUND_WITH_ID + id));

        return mapper.toResponse(asset);
    }

    /**
     * Returns a page of assets.
     *
     * @param pageable - A Pageable object, determines the page, size and sort
     * @return a page of Asset records
     */
    @Override
    public Page<AssetResponseDTO> getAllAssets(AssetFilter filter, Pageable pageable) {

        Specification<Asset> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (filter.getName() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }

        if (filter.getCategoryId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category").get("id"), filter.getCategoryId()));
        }

        if (filter.getLocation() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("location")), "%" + filter.getLocation().toLowerCase() + "%"));
        }

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), filter.getStatus()));
        }

        Page<Asset> assets = repository.findAll(spec, pageable);

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
    @Transactional(rollbackFor = Exception.class)
    public AssetResponseDTO updateAsset(Long id, AssetUpdateRequestDTO assetRequest) {

        Asset asset = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ASSET_NOT_FOUND_WITH_ID + id));

        if (assetRequest.categoryId() != null) {
            AssetCategory category = assetCategoryRepository.findById(assetRequest.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("AssetCategory does not exist for id: " + assetRequest.categoryId()));

            asset.setCategory(category);
        }

        mapper.updateEntityFromDto(assetRequest, asset);

        asset = repository.save(asset);

        return mapper.toResponse(asset);
    }

    /**
     * Update the QR Code for the specified asset only.
     *
     * @param id - a Long id
     * @param filePath - path to the QR Code
     * @return an Asset record
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AssetResponseDTO updateAssetQRCode(Long id, String filePath) {

        Asset asset = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ASSET_NOT_FOUND_WITH_ID + id));

        asset.setCode(filePath);
        asset = repository.save(asset);

        return mapper.toResponse(asset);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteAsset(Long id) {

        Asset asset = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ASSET_NOT_FOUND_WITH_ID + id));

        asset.setStatus(AssetStatusEnum.DELETED);

        repository.save(asset);
    }

    @Override
    public Asset getActiveAssetById(Long id) {
        return repository.findByIdAndStatus(id, AssetStatusEnum.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ASSET_NOT_FOUND_WITH_ID + id + " and status ACTIVE"));
    }
}