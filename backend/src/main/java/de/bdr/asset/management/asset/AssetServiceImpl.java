package de.bdr.asset.management.asset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Transactional(readOnly = true)
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
    @Transactional(rollbackFor = Exception.class)
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

        boolean isAdmin = false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {

            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(r -> "ROLE_ADMIN".equals(r.getAuthority()));
        }

        Asset asset;

        if (isAdmin) {

            asset = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        }
        else {

            asset = repository.findByIdAndStatusNot(id, AssetStatusEnum.DELETED)
                    .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        }

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
    public Page<AssetResponseDTO> getAllAssets(AssetFilter filter, Pageable pageable) {

        log.debug("Fetching assets from the database with pagination: " +
                        "Page number: {} | Page size: {} | Sort: {}",
                        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()
        );

        boolean isAdmin = false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(r -> "ROLE_ADMIN".equals(r.getAuthority()));
        }

        Specification<Asset> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (!isAdmin) {
            spec = spec.and((root, query, cb) ->
                    cb.notEqual(root.get("status"), AssetStatusEnum.DELETED));
        }

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
    @Transactional(rollbackFor = Exception.class)
    public AssetResponseDTO updateAsset(Long id, AssetRequestDTO assetRequest) {

        log.info("Attempting to update asset with id: {}", id);

        Asset asset = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        AssetCategory category = assetCategoryRepository.findById(assetRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("AssetCategory does not exist for id: " + assetRequest.categoryId()));

        asset.setName(assetRequest.name());
        asset.setDescription(assetRequest.description());
        asset.setStatus(assetRequest.status());
        asset.setCategory(category);
        asset = repository.save(asset);

        log.info("Successfully updated asset with id: {}", id);
        
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

        log.info("Attempting to update asset QR Code with id: {}", id);

        Asset asset = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        asset.setCode(filePath);
        asset = repository.save(asset);

        log.info("Successfully updated asset QR Code with id: {}", id);
        
        return mapper.toResponse(asset);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteAsset(Long id) {

        log.info("Attempting to delete asset with id: {}", id);

        Asset asset = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        asset.setStatus(AssetStatusEnum.DELETED);

        repository.save(asset);
    }
}