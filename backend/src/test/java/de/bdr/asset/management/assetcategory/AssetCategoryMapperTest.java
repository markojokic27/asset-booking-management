package de.bdr.asset.management.assetcategory;

import de.bdr.asset.management.assetcategory.dto.AssetCategoryRequestDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryResponseDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryUpdateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AssetCategoryMapperTest {

    private AssetCategoryMapper assetCategoryMapper;

    @BeforeEach
    void setUp() {
        assetCategoryMapper = new AssetCategoryMapperImpl();
    }

    private AssetCategoryRequestDTO buildRequest() {
        return new AssetCategoryRequestDTO(
                "Electronics",
                "A category for electronic devices",
                BookingPeriodEnum.DAY,
                true
        );
    }

    private AssetCategory buildCategory() {
        AssetCategory category = new AssetCategory();
        category.setId(1L);
        category.setName("Electronics");
        category.setDescription("A category for electronic devices");
        category.setBookingPeriod(BookingPeriodEnum.DAY);
        category.setApproval(true);
        return category;
    }

    // --- toEntity ---

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        AssetCategory result = assetCategoryMapper.toEntity(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldMapNameToEntity() {
        AssetCategory result = assetCategoryMapper.toEntity(buildRequest());
        assertThat(result.getName()).isEqualTo("Electronics");
    }

    @Test
    void shouldMapDescriptionToEntity() {
        AssetCategory result = assetCategoryMapper.toEntity(buildRequest());
        assertThat(result.getDescription()).isEqualTo("A category for electronic devices");
    }

    @Test
    void shouldMapBookingPeriodToEntity() {
        AssetCategory result = assetCategoryMapper.toEntity(buildRequest());
        assertThat(result.getBookingPeriod()).isEqualTo(BookingPeriodEnum.DAY);
    }

    @Test
    void shouldMapApprovalToEntity() {
        AssetCategory result = assetCategoryMapper.toEntity(buildRequest());
        assertThat(result.isApproval()).isTrue();
    }

    @Test
    void shouldIgnoreIdWhenMappingToEntity() {
        AssetCategory result = assetCategoryMapper.toEntity(buildRequest());
        assertThat(result.getId()).isNull();
    }

    @Test
    void shouldIgnoreCreatedAtWhenMappingToEntity() {
        AssetCategory result = assetCategoryMapper.toEntity(buildRequest());
        assertThat(result.getCreatedAt()).isNull();
    }

    @Test
    void shouldIgnoreLastModifiedAtWhenMappingToEntity() {
        AssetCategory result = assetCategoryMapper.toEntity(buildRequest());
        assertThat(result.getLastModifiedAt()).isNull();
    }

    @Test
    void shouldMapNullDescriptionToEntity() {
        AssetCategoryRequestDTO request = new AssetCategoryRequestDTO(
                "Electronics",
                null,
                BookingPeriodEnum.DAY,
                true
        );
        AssetCategory result = assetCategoryMapper.toEntity(request);
        assertThat(result.getDescription()).isNull();
    }

    // --- toResponse ---

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        AssetCategoryResponseDTO result = assetCategoryMapper.toResponse(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldMapIdToResponse() {
        AssetCategoryResponseDTO result = assetCategoryMapper.toResponse(buildCategory());
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void shouldMapNameToResponse() {
        AssetCategoryResponseDTO result = assetCategoryMapper.toResponse(buildCategory());
        assertThat(result.name()).isEqualTo("Electronics");
    }

    @Test
    void shouldMapDescriptionToResponse() {
        AssetCategoryResponseDTO result = assetCategoryMapper.toResponse(buildCategory());
        assertThat(result.description()).isEqualTo("A category for electronic devices");
    }

    @Test
    void shouldMapBookingPeriodToResponse() {
        AssetCategoryResponseDTO result = assetCategoryMapper.toResponse(buildCategory());
        assertThat(result.bookingPeriod()).isEqualTo(BookingPeriodEnum.DAY);
    }

    @Test
    void shouldMapApprovalToResponse() {
        AssetCategoryResponseDTO result = assetCategoryMapper.toResponse(buildCategory());
        assertThat(result.approval()).isTrue();
    }

    @Test
    void shouldMapNullDescriptionToResponse() {
        AssetCategory category = buildCategory();
        category.setDescription(null);

        AssetCategoryResponseDTO result = assetCategoryMapper.toResponse(category);
        assertThat(result.description()).isNull();
    }

    @Test
    void shouldMapApprovalFalseToResponse() {
        AssetCategory category = buildCategory();
        category.setApproval(false);

        AssetCategoryResponseDTO result = assetCategoryMapper.toResponse(category);
        assertThat(result.approval()).isFalse();
    }

    // --- updateEntityFromDto ---

    @Test
    void shouldUpdateAllFieldsFromDto() {
        AssetCategoryUpdateRequestDTO updateDto = new AssetCategoryUpdateRequestDTO(
                "UpdatedName", "Updated description", BookingPeriodEnum.WEEK, false
        );
        AssetCategory entity = buildCategory();

        assetCategoryMapper.updateEntityFromDto(updateDto, entity);

        assertThat(entity.getName()).isEqualTo("UpdatedName");
        assertThat(entity.getDescription()).isEqualTo("Updated description");
        assertThat(entity.getBookingPeriod()).isEqualTo(BookingPeriodEnum.WEEK);
        assertThat(entity.isApproval()).isFalse();
    }

    @Test
    void shouldOnlyUpdateNonNullFields() {
        AssetCategoryUpdateRequestDTO updateDto = new AssetCategoryUpdateRequestDTO(
                "OnlyName", null, null, null
        );
        AssetCategory entity = buildCategory();

        assetCategoryMapper.updateEntityFromDto(updateDto, entity);

        assertThat(entity.getName()).isEqualTo("OnlyName");
        assertThat(entity.getDescription()).isEqualTo("A category for electronic devices");
        assertThat(entity.getBookingPeriod()).isEqualTo(BookingPeriodEnum.DAY);
        assertThat(entity.isApproval()).isTrue();
    }

    @Test
    void shouldNotChangeEntityWhenAllFieldsAreNull() {
        AssetCategoryUpdateRequestDTO updateDto = new AssetCategoryUpdateRequestDTO(
                null, null, null, null
        );
        AssetCategory entity = buildCategory();

        assetCategoryMapper.updateEntityFromDto(updateDto, entity);

        assertThat(entity.getName()).isEqualTo("Electronics");
        assertThat(entity.getDescription()).isEqualTo("A category for electronic devices");
        assertThat(entity.getBookingPeriod()).isEqualTo(BookingPeriodEnum.DAY);
        assertThat(entity.isApproval()).isTrue();
    }
}