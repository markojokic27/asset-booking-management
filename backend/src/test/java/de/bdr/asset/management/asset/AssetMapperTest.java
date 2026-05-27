package de.bdr.asset.management.asset;

import static org.assertj.core.api.Assertions.assertThat;

import de.bdr.asset.management.asset.dtos.AssetRequestDTO;
import de.bdr.asset.management.asset.dtos.AssetResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.bdr.asset.management.assetcategory.AssetCategory;

public class AssetMapperTest {

    private AssetMapper assetMapper;

    @BeforeEach
    void setUp() {
        assetMapper = new AssetMapperImpl();
    }

    private AssetRequestDTO buildRequest() {
        return new AssetRequestDTO(
                "Laptop Dell",
                1L,
                "A high-performance laptop",
                AssetStatusEnum.ACTIVE,
                "Office A"
        );
    }

    private Asset buildAsset() {
        AssetCategory category = new AssetCategory();
        category.setId(42L);

        Asset asset = new Asset();
        asset.setId(1L);
        asset.setName("Laptop Dell");
        asset.setCategory(category);
        asset.setDescription("A high-performance laptop");
        asset.setCode("QR-CODE-123");
        asset.setStatus(AssetStatusEnum.ACTIVE);
        asset.setLocation("Office A");
        return asset;
    }

    // --- toEntity ---

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        Asset result = assetMapper.toEntity(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldMapNameToEntity() {
        Asset result = assetMapper.toEntity(buildRequest());
        assertThat(result.getName()).isEqualTo("Laptop Dell");
    }

    @Test
    void shouldMapDescriptionToEntity() {
        Asset result = assetMapper.toEntity(buildRequest());
        assertThat(result.getDescription()).isEqualTo("A high-performance laptop");
    }

    @Test
    void shouldMapCodeToEntity() {
        Asset result = assetMapper.toEntity(buildRequest());
        assertThat(result.getCode()).isNull();
    }

    @Test
    void shouldMapStatusToEntity() {
        Asset result = assetMapper.toEntity(buildRequest());
        assertThat(result.getStatus()).isEqualTo(AssetStatusEnum.ACTIVE);
    }

    @Test
    void shouldMapLocationToEntity() {
        Asset result = assetMapper.toEntity(buildRequest());
        assertThat(result.getLocation()).isEqualTo("Office A");
    }

    @Test
    void shouldIgnoreIdWhenMappingToEntity() {
        Asset result = assetMapper.toEntity(buildRequest());
        assertThat(result.getId()).isNull();
    }

    @Test
    void shouldIgnoreCategoryWhenMappingToEntity() {
        Asset result = assetMapper.toEntity(buildRequest());
        assertThat(result.getCategory()).isNull();
    }

    @Test
    void shouldIgnoreCreatedAtWhenMappingToEntity() {
        Asset result = assetMapper.toEntity(buildRequest());
        assertThat(result.getCreatedAt()).isNull();
    }

    @Test
    void shouldIgnoreLastModifiedAtWhenMappingToEntity() {
        Asset result = assetMapper.toEntity(buildRequest());
        assertThat(result.getLastModifiedAt()).isNull();
    }

    // --- toResponse ---

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        AssetResponseDTO result = assetMapper.toResponse(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldMapIdToResponse() {
        AssetResponseDTO result = assetMapper.toResponse(buildAsset());
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void shouldMapNameToResponse() {
        AssetResponseDTO result = assetMapper.toResponse(buildAsset());
        assertThat(result.name()).isEqualTo("Laptop Dell");
    }

    @Test
    void shouldMapCategoryIdFromNestedCategory() {
        AssetResponseDTO result = assetMapper.toResponse(buildAsset());
        assertThat(result.categoryId()).isEqualTo(42L);
    }

    @Test
    void shouldSetCategoryIdToNullWhenCategoryIsNull() {
        Asset asset = buildAsset();
        asset.setCategory(null);

        AssetResponseDTO result = assetMapper.toResponse(asset);
        assertThat(result.categoryId()).isNull();
    }

    @Test
    void shouldMapDescriptionToResponse() {
        AssetResponseDTO result = assetMapper.toResponse(buildAsset());
        assertThat(result.description()).isEqualTo("A high-performance laptop");
    }

    @Test
    void shouldMapCodeToResponse() {
        AssetResponseDTO result = assetMapper.toResponse(buildAsset());
        assertThat(result.code()).isEqualTo("QR-CODE-123");
    }

    @Test
    void shouldMapStatusToResponse() {
        AssetResponseDTO result = assetMapper.toResponse(buildAsset());
        assertThat(result.status()).isEqualTo(AssetStatusEnum.ACTIVE);
    }

    @Test
    void shouldMapLocationToResponse() {
        AssetResponseDTO result = assetMapper.toResponse(buildAsset());
        assertThat(result.location()).isEqualTo("Office A");
    }

    @Test
    void shouldMapNullDescriptionToResponse() {
        Asset asset = buildAsset();
        asset.setDescription(null);

        AssetResponseDTO result = assetMapper.toResponse(asset);
        assertThat(result.description()).isNull();
    }

    @Test
    void shouldMapNullCodeToResponse() {
        Asset asset = buildAsset();
        asset.setCode(null);

        AssetResponseDTO result = assetMapper.toResponse(asset);
        assertThat(result.code()).isNull();
    }
}