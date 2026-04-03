package de.bdr.asset.management.asset;

import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.assetcategory.AssetCategoryRepository;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import de.bdr.asset.management.feature.FeatureConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository repository;

    @Mock
    private FeatureConfig featureConfig;

    @Mock
    private AssetMapper mapper;

    @Mock
    private AssetCategoryRepository assetCategoryRepository;

    @InjectMocks
    private AssetServiceImpl service;

    private Asset asset;
    private AssetCategory category;
    private AssetRequestDTO requestDTO;
    private AssetResponseDTO responseDTO;

    // Initialize common test data used in all tests
    @BeforeEach
    void setUp() {
        category = new AssetCategory();
        category.setId(1L);

        asset = new Asset();
        asset.setId(1L);
        asset.setName("Hp 15");

        requestDTO = new AssetRequestDTO(
                "Hp 15",
                1L,
                "Laptop located in room 301",
                "QR-LAPTOP-001",
                AssetStatusEnum.ACTIVE,
                "Room 301"

        );

        responseDTO = new AssetResponseDTO(
                1L,
                "Hp 15",
                1L,
                "Laptop located in room 301",
                "QR-LAPTOP-001",
                AssetStatusEnum.ACTIVE,
                "Room 301"

        );
    }

    // Tests createAsset(): category exists → map request, save asset, return response
    @Test
    void shouldCreateAsset() {
        when(featureConfig.isAssetNameValidationEnabled()).thenReturn(false);

        when(assetCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.toEntity(requestDTO)).thenReturn(asset);
        when(repository.save(asset)).thenReturn(asset);
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        AssetResponseDTO result = service.createAsset(requestDTO);

        assertNotNull(result);
        assertEquals("Hp 15", result.name());

        verify(repository).save(asset);
        verify(mapper).toResponse(asset);
    }

    @Test
    void shouldCreateAssetWithNewFeature() {
        when(featureConfig.isAssetNameValidationEnabled()).thenReturn(true); // ← novi path

        when(assetCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.toEntity(requestDTO)).thenReturn(asset);
        when(repository.save(asset)).thenReturn(asset);
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        AssetResponseDTO result = service.createAsset(requestDTO);

        assertNotNull(result);
        verify(repository).save(asset);
    }

    // Tests createAsset(): throws exception if AssetCategory does not exist
    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {

        when(assetCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createAsset(requestDTO));

        verify(repository, never()).save(any());
    }

    // Tests getAssetById(): asset found → mapped to response DTO
    @Test
    void shouldGetAssetById() {

        when(repository.findById(1L)).thenReturn(Optional.of(asset));
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        AssetResponseDTO result = service.getAssetById(1L);

        assertEquals(1L, result.id());

        verify(repository).findById(1L);
    }

    // Tests getAssetById(): throws exception if asset not found
    @Test
    void shouldThrowExceptionWhenAssetNotFound() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getAssetById(1L));
    }

    // Tests getAllAssets(): fetch all assets and map them to response DTOs
    @Test
    void shouldReturnAllAssets() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Asset> assetPage = new PageImpl<>(List.of(asset));

        when(repository.findAll(pageable)).thenReturn(assetPage);
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        Page<AssetResponseDTO> result = service.getAllAssets(pageable);
        Page<AssetResponseDTO> result = service.getAllAssets(pageable);

        assertEquals(1, result.getContent().size());

        verify(repository).findAll(pageable);
        verify(repository).findAll(pageable);
    }

    // Tests updateAsset(): asset and category exist → update fields, save, return response
    @Test
    void shouldUpdateAsset() {

        when(repository.findById(1L)).thenReturn(Optional.of(asset));
        when(assetCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.save(asset)).thenReturn(asset);
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        AssetResponseDTO result = service.updateAsset(1L, requestDTO);

        assertEquals("Hp 15", result.name());

        verify(repository).save(asset);
    }

    // Tests updateAsset(): throws exception if asset does not exist
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingAsset() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateAsset(1L, requestDTO));
    }
}