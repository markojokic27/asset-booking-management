package de.bdr.asset.management.assetcategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bdr.asset.management.asset.AssetRepository;
import de.bdr.asset.management.core.exception.ActionNotAllowedException;
import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

/** Implementation of AssetCategory Service */
@Slf4j
@Service
@Transactional(readOnly = true)
public class AssetCategoryServiceImpl implements AssetCategoryService {
    private static final String NOT_FOUND = "AssetCategory not found with id: ";

    private final AssetCategoryRepository repository;
    private final AssetCategoryMapper mapper;
    private final AssetRepository assetRepository;

    public AssetCategoryServiceImpl(AssetCategoryRepository repository, AssetCategoryMapper mapper, AssetRepository assetRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.assetRepository = assetRepository;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AssetCategoryResponseDTO createAssetCategory(AssetCategoryRequestDTO assetCategoryRequest){

        if (repository.existsByName(assetCategoryRequest.name())) {
            throw new DuplicateResourceException("Asset category " + assetCategoryRequest.name() + " already exists.");
        }

        AssetCategory category = mapper.toEntity(assetCategoryRequest);
        category = repository.save(category);

        return mapper.toResponse(category);
    }

    /** {@inheritDoc} */
    @Override
    public AssetCategoryResponseDTO getAssetCategoryById(Long id){

        AssetCategory category = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));

        return mapper.toResponse(category);
    }

    /** {@inheritDoc} */
    @Override
    public Page<AssetCategoryResponseDTO> getAllAssetCategories(Pageable pageable){

        Page<AssetCategory> categories = repository.findAll(pageable);

        return categories.map(mapper::toResponse);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AssetCategoryResponseDTO updateAssetCategory(Long id, AssetCategoryRequestDTO assetCategoryRequest){

        AssetCategory category = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));

        if (repository.existsByNameAndIdNot(assetCategoryRequest.name(), id)) {
            throw new DuplicateResourceException("Asset category " + assetCategoryRequest.name() + " already exists.");
        }

        category.setName(assetCategoryRequest.name());
        category.setDescription(assetCategoryRequest.description());
        category.setBookingPeriod(assetCategoryRequest.bookingPeriod());
        category.setApproval(assetCategoryRequest.approval());
        AssetCategory updatedCategory = repository.save(category);

        return mapper.toResponse(updatedCategory);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAssetCategory(Long id) {

        AssetCategory category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));

        boolean hasAssets = assetRepository.existsByCategoryId(id);

        if (hasAssets) {
            throw new ActionNotAllowedException("Cannot delete category because assets are assigned to it.");
        }

        repository.delete(category);
    }
}
