package de.bdr.asset.management.assetcategory;

import de.bdr.asset.management.asset.AssetRepository;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryRequestDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryResponseDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryUpdateRequestDTO;
import de.bdr.asset.management.core.exception.ActionNotAllowedException;
import de.bdr.asset.management.core.exception.DuplicateResourceException;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetCategoryServiceImplTest {

    @Mock
    private AssetCategoryRepository repository;

    @Mock
    private AssetCategoryMapper mapper;

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetCategoryServiceImpl service;

    private AssetCategory category;
    private AssetCategoryRequestDTO requestDTO;
    private AssetCategoryUpdateRequestDTO updateRequestDTO;
    private AssetCategoryResponseDTO responseDTO;

    // Prepare test data
    @BeforeEach
    void setUp() {
        category = new AssetCategory();
        category.setId(1L);
        category.setName("Books");

        requestDTO = new AssetCategoryRequestDTO(
                "Books",
                "A collection of books available for borrowing within the company library.",
                BookingPeriodEnum.DAY,
                Boolean.TRUE
        );

        updateRequestDTO = new AssetCategoryUpdateRequestDTO(
                "Books",
                "A collection of books available for borrowing within the company library.",
                BookingPeriodEnum.DAY,
                Boolean.TRUE
        );

        responseDTO = new AssetCategoryResponseDTO(
                1L,
                "Books",
                "A collection of books available for borrowing within the company library.",
                BookingPeriodEnum.DAY,
                Boolean.TRUE
        );
    }

    // Tests createAssetCategory(): map request, save entity, return response
    @Test
    void shouldCreateAssetCategory() {

        when(mapper.toEntity(requestDTO)).thenReturn(category);
        when(repository.save(category)).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(responseDTO);

        AssetCategoryResponseDTO result = service.createAssetCategory(requestDTO);

        assertNotNull(result);
        assertEquals("Books", result.name());

        verify(repository).save(category);
        verify(mapper).toResponse(category);
    }

    // Tests getAssetCategoryById(): category found and mapped to response DTO
    @Test
    void shouldGetAssetCategoryById() {

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.toResponse(category)).thenReturn(responseDTO);

        AssetCategoryResponseDTO result = service.getAssetCategoryById(1L);

        assertEquals(1L, result.id());

        verify(repository).findById(1L);
    }

    // Tests getAssetCategoryById(): throws exception if category not found
    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getAssetCategoryById(1L));
    }

    // Tests getAllAssetCategories(): fetch paginated categories and map to DTOs
    @Test
    void shouldReturnAllAssetCategories() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<AssetCategory> categoryPage = new PageImpl<>(java.util.List.of(category));

        when(repository.findAll(pageable)).thenReturn(categoryPage);
        when(mapper.toResponse(category)).thenReturn(responseDTO);

        Page<AssetCategoryResponseDTO> result = service.getAllAssetCategories(pageable);

        assertEquals(1, result.getTotalElements());

        verify(repository).findAll(pageable);
    }

    // Tests updateAssetCategory(): category exists, fields updated and saved
    @Test
    void shouldUpdateAssetCategory() {

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.existsByNameAndIdNot("Books", 1L)).thenReturn(false);
        when(repository.save(category)).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(responseDTO);

        AssetCategoryResponseDTO result = service.updateAssetCategory(1L, updateRequestDTO);

        assertEquals("Books", result.name());

        verify(mapper).updateEntityFromDto(updateRequestDTO, category);
        verify(repository).save(category);
    }

    // Tests updateAssetCategory(): throws exception if category does not exist
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingCategory() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateAssetCategory(1L, updateRequestDTO));
    }

    // Tests createAssetCategory(): throws exception if category name already exists
    @Test
    void shouldThrowExceptionWhenCreatingDuplicateCategory() {

        when(repository.existsByName(requestDTO.name())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.createAssetCategory(requestDTO));

        verify(repository).existsByName(requestDTO.name());
        verify(repository, never()).save(any());
    }

    // Tests updateAssetCategory(): throws exception if new name already exists for a DIFFERENT category
    @Test
    void shouldThrowExceptionWhenUpdatingDuplicateCategoryName() {

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.existsByNameAndIdNot(updateRequestDTO.name(), 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.updateAssetCategory(1L, updateRequestDTO));

        verify(repository).findById(1L);
        verify(repository).existsByNameAndIdNot(requestDTO.name(), 1L);
        verify(repository, never()).save(any());
    }

    // Tests deleteAssetCategory(): category exists, no assets assigned, should delete
    @Test
    void shouldDeleteAssetCategory() {
        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(assetRepository.existsByCategoryId(1L)).thenReturn(false);

        service.deleteAssetCategory(1L);

        verify(repository).findById(1L);
        verify(assetRepository).existsByCategoryId(1L);
        verify(repository).delete(category);
    }

    // Tests deleteAssetCategory(): category not found
    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCategory() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.deleteAssetCategory(1L));
    }

    // Tests deleteAssetCategory(): category has assets assigned
    @Test
    void shouldThrowExceptionWhenCategoryHasAssets() {
        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(assetRepository.existsByCategoryId(1L)).thenReturn(true);

        assertThrows(ActionNotAllowedException.class,
                () -> service.deleteAssetCategory(1L));

        verify(repository, never()).delete(any());
    }
}