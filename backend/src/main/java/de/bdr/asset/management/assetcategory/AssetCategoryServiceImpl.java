package de.bdr.asset.management.assetcategory;

import de.bdr.asset.management.core.exception.DuplicateResourceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of AssetCategory Service
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class AssetCategoryServiceImpl implements AssetCategoryService {
    private final AssetCategoryRepository repository;
    private final AssetCategoryMapper mapper;

    public AssetCategoryServiceImpl(AssetCategoryRepository repository, AssetCategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Create asset category in DB.
     *
     * @param assetCategoryRequest - a AssetCategoryDTO record
     * @return an AssetCategoryResponseDTO record
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AssetCategoryResponseDTO createAssetCategory(AssetCategoryRequestDTO assetCategoryRequest){

        log.info("Attempting to create a new asset category");

        if (repository.existsByName(assetCategoryRequest.name())) {
            throw new DuplicateResourceException("Asset category " + assetCategoryRequest.name() + " already exists.");
        }

        AssetCategory category = mapper.toEntity(assetCategoryRequest);
        category = repository.save(category);

        log.info("Successfully created new asset category with id: {}", category.getId());

        return mapper.toResponse(category);
    }

    /**
     * Returns a specific asset category.
     *
     * @param id - a Long id
     * @return an AssetCategoryResponseDTO record
     */
    @Override
    public AssetCategoryResponseDTO getAssetCategoryById(Long id){

        AssetCategory category = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AssetCategory not found with id: " + id));

        log.info("Asset category found with id: {}", id);

        return mapper.toResponse(category);
    }

    /**
     * Returns a page of asset categories.
     *
     * @param pageable - A Pageable object, determines the page, size and sort
     * @return a page of AssetCategoryResponseDTO records
     */
    @Override
    public Page<AssetCategoryResponseDTO> getAllAssetCategories(Pageable pageable){

        log.debug("Fetching asset categories from the database with pagination: " +
                        "Page number: {} | Page size: {} | Sort: {}",
                        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()
        );

        Page<AssetCategory> categories = repository.findAll(pageable);

        log.info("Successfully fetched {} asset categories", categories.getNumberOfElements());

        return categories.map(mapper::toResponse);
    }

    /**
     * Update and return a specific asset category.
     *
     * @param id - a Long id
     * @param assetCategoryRequest - an AssetCategoryDTO record
     * @return an AssetCategoryResponseDTO record
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AssetCategoryResponseDTO updateAssetCategory(Long id, AssetCategoryRequestDTO assetCategoryRequest){

        log.info("Attempting to update asset category with id: {}", id);

        AssetCategory category = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AssetCategory not found with id: " + id));

        if (repository.existsByNameAndIdNot(assetCategoryRequest.name(), id)) {
            throw new DuplicateResourceException("Asset category " + assetCategoryRequest.name() + " already exists.");
        }

        category.setName(assetCategoryRequest.name());
        category.setDescription(assetCategoryRequest.description());
        category.setBookingPeriod(assetCategoryRequest.bookingPeriod());
        category.setApproval(assetCategoryRequest.approval());
        AssetCategory updatedCategory = repository.save(category);

        log.info("Successfully updated asset category with id: {}", id);

        return mapper.toResponse(updatedCategory);
    }

    /**
     * Delete a specific asset category.
     *
     * @param id - a Long id
     * @implNote Should be a soft delete by setting it to inactive or such
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAssetCategory(Long id){

        // TODO: Add a field for soft delete

        // AssetCategory category = repository.findById(id)
        //     .orElseThrow(() -> new ResourceNotFoundException("AssetCategory not found with id:" + id));

        // category.setStatus("DELETED"),

        // repository.save(category);
    }
}
