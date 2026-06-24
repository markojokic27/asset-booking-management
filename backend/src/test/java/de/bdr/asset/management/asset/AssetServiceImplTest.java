package de.bdr.asset.management.asset;

import java.util.List;
import java.util.Optional;

import de.bdr.asset.management.asset.dtos.AssetRequestDTO;
import de.bdr.asset.management.asset.dtos.AssetResponseDTO;
import de.bdr.asset.management.asset.dtos.AssetUpdateRequestDTO;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.assetcategory.AssetCategoryRepository;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository repository;

    @Mock
    private AssetMapper mapper;

    @Mock
    private AssetCategoryRepository assetCategoryRepository;

    @InjectMocks
    private AssetServiceImpl service;

    private Asset asset;
    private AssetCategory category;
    private AssetRequestDTO requestDTO;
    private AssetUpdateRequestDTO updateRequestDTO;
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
                AssetStatusEnum.ACTIVE,
                "Room 301"

        );

        updateRequestDTO = new AssetUpdateRequestDTO(
                "Hp 15",
                1L,
                "Laptop located in room 301",
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

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // Tests createAsset(): category exists → map request, save asset, return response
    @Test
    void shouldCreateAsset() {

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

    // Tests createAsset(): throws exception if AssetCategory does not exist
    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {

        when(assetCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createAsset(requestDTO));

        verify(repository, never()).save(any());
    }

    // Tests getAssetById(): user admin -> asset found → mapped to response DTO
    @Test
    void shouldGetAssetById_WhenUserIsAdmin() {

        mockLoggedUser("ROLE_ADMIN");

        when(repository.findById(1L)).thenReturn(Optional.of(asset));
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        AssetResponseDTO result = service.getAssetById(1L);

        assertEquals(1L, result.id());

        verify(repository).findById(1L);
    }

    // Tests getAssetById(): user employee or manager -> asset found → mapped to response DTO
    @Test
    void shouldGetAssetById_WhenUserIsNotAdmin() {

        mockLoggedUser("ROLE_EMPLOYEE");

        when(repository.findById(1L)).thenReturn(Optional.of(asset));
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        AssetResponseDTO result = service.getAssetById(1L);

        assertEquals(1L, result.id());

        verify(repository).findById(1L);
    }

    // Tests getAssetById(): throws exception if asset not found if user is admin
    @Test
    void shouldThrowExceptionWhenAssetNotFound_WhenUserIsAdmin() {

        mockLoggedUser("ROLE_ADMIN");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getAssetById(1L));
    }

    // Tests getAssetById(): throws exception if asset not found if user is employee or manager
    @Test
    void shouldThrowExceptionWhenAssetNotFound_WhenUserIsNotAdmin() {

        mockLoggedUser("ROLE_EMPLOYEE");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getAssetById(1L));
    }

    // Tests getAssetById(): user is not authenticated (auth is null) -> defaults to non-admin behavior
    @Test
    void shouldGetAssetById_WhenUserIsNotAuthenticated() {

        SecurityContextHolder.clearContext();

        when(repository.findById(1L)).thenReturn(Optional.of(asset));
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        AssetResponseDTO result = service.getAssetById(1L);

        assertEquals(1L, result.id());


        verify(repository).findById(1L);
    }

    // Tests getAssetById(): throws exception when user is not authenticated and asset not found
    @Test
    void shouldThrowExceptionWhenAssetNotFound_WhenUserIsNotAuthenticated() {

        SecurityContextHolder.clearContext();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getAssetById(1L));

        verify(repository).findById(1L);
    }

    // Tests getAllAssets(): if user is admin fetch all assets and map them to response DTOs
    @Test
    void shouldReturnAllAssets_WhenUserIsAdmin() {

        mockLoggedUser("ROLE_ADMIN");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Asset> assetPage = new PageImpl<>(List.of(asset));

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(assetPage);
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        Page<AssetResponseDTO> result = service.getAllAssets(new AssetFilter(), pageable);

        assertEquals(1, result.getContent().size());

        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    // Tests getAllAssets(): if user is employee or manager fetch all assets that are not deleted and map them to response DTOs
    @Test
    void shouldReturnAllAssets_WhenUserIsNotAdmin() {

        mockLoggedUser("ROLE_EMPLOYEE");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Asset> assetPage = new PageImpl<>(List.of(asset));

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(assetPage);
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        Page<AssetResponseDTO> result = service.getAllAssets(new AssetFilter(), pageable);

        assertEquals(1, result.getContent().size());

        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    // Tests updateAsset(): asset and category exist → update fields, save, return response
    @Test
    void shouldUpdateAsset() {

        when(repository.findById(1L)).thenReturn(Optional.of(asset));
        when(assetCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.save(asset)).thenReturn(asset);
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        AssetResponseDTO result = service.updateAsset(1L, updateRequestDTO);

        assertEquals("Hp 15", result.name());
        verify(mapper).updateEntityFromDto(updateRequestDTO, asset);
        verify(repository).save(asset);
    }


    // Tests updateAsset(): throws exception if asset does not exist
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingAsset() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateAsset(1L, updateRequestDTO));
    }

    // Tests softDeleteAsset(): asset exist → change status to DELETED
    @Test
    void shouldSoftDeleteAsset() {

        when(repository.findById(1L)).thenReturn(Optional.of(asset));

        service.softDeleteAsset(1L);

        assertEquals(AssetStatusEnum.DELETED, asset.getStatus());
        verify(repository).findById(1L);
        verify(repository).save(asset);
    }

    // Tests softDeleteAsset(): throws exception if asset does not exist
    @Test
    void shouldThrowExceptionWhenSoftDeletingNonExistingAsset() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.softDeleteAsset(1L));

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    // Tests updateAssetQRCode(): asset exists → update code (filePath), save, map to response
    @Test
    void shouldUpdateAssetQRCode() {

        String filePath = "src/main/resources/qr-codes/1.png";

        when(repository.findById(1L)).thenReturn(Optional.of(asset));
        when(repository.save(asset)).thenReturn(asset);
        when(mapper.toResponse(asset)).thenReturn(responseDTO);

        AssetResponseDTO result = service.updateAssetQRCode(1L, filePath);

        assertNotNull(result);
        assertEquals(filePath, asset.getCode());

        verify(repository).findById(1L);
        verify(repository).save(asset);
        verify(mapper).toResponse(asset);
    }

    // Tests updateAssetQRCode(): throws exception if asset does not exist
    @Test
    void shouldThrowExceptionWhenUpdatingQRCodeForNonExistingAsset() {

        String filePath = "src/main/resources/qr-codes/1.png";

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateAssetQRCode(1L, filePath));
        
        verify(repository).findById(1L);
        verify(repository, never()).save(any());
        verify(mapper, never()).toResponse(any());
    }

    // Tests getActiveAssetById(): active asset exists → returns asset entity
    @Test
    void shouldGetActiveAssetById() {

        when(repository.findByIdAndStatus(1L, AssetStatusEnum.ACTIVE)).thenReturn(Optional.of(asset));

        Asset result = service.getActiveAssetById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository).findByIdAndStatus(1L, AssetStatusEnum.ACTIVE);
    }

    // Tests getActiveAssetById(): throws exception if no active asset found
    @Test
    void shouldThrowExceptionWhenActiveAssetNotFound() {

        when(repository.findByIdAndStatus(1L, AssetStatusEnum.ACTIVE)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getActiveAssetById(1L));

        verify(repository).findByIdAndStatus(1L, AssetStatusEnum.ACTIVE);
    }

    private void mockLoggedUser(String user) {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        lenient().doReturn(List.of(new SimpleGrantedAuthority(user))).when(authentication).getAuthorities();
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

}