package de.bdr.asset.management.assetcategory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import de.bdr.asset.management.assetcategory.dto.AssetCategoryRequestDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryResponseDTO;
import de.bdr.asset.management.assetcategory.dto.AssetCategoryUpdateRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AssetCategoryControllerTest {
    @Mock
    private AssetCategoryService assetCategoryService;

    @InjectMocks
    private AssetCategoryController assetCategoryController;

    /** CREATE */
    @Test
    void createAssetCategory_validRequest_returnsCreatedStatus(){
        AssetCategoryRequestDTO request = new AssetCategoryRequestDTO( "Book", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);
        AssetCategoryResponseDTO response = new AssetCategoryResponseDTO( 1L, "Books", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);

        when(assetCategoryService.createAssetCategory(request)).thenReturn(response);

        ResponseEntity<AssetCategoryResponseDTO> result = assetCategoryController.create(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        verify(assetCategoryService).createAssetCategory(request);

    }

    /** READ ALL */
    @Test
    void getAllAssetCategories_returnsOkWithPage() {
        AssetCategoryResponseDTO response =
            new AssetCategoryResponseDTO(
                1L,
                "Books",
                "A collection of books available for borrowing within the company library.",
                BookingPeriodEnum.DAY,
                Boolean.TRUE
            );

        List<AssetCategoryResponseDTO> list = List.of(response);
        Page<AssetCategoryResponseDTO> page = new PageImpl<>(list);

        when(assetCategoryService.getAllAssetCategories(any(Pageable.class)))
            .thenReturn(page);

        ResponseEntity<Page<AssetCategoryResponseDTO>> result =
            assetCategoryController.getAll(PageRequest.of(0, 10));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert result.getBody() != null;
        assertThat(result.getBody().getContent())
            .hasSize(1)
            .contains(response);
    }

    /** READ BY ID */
    @Test
    void getAssetCategoryById_returnsOkWithAssetCategory(){
        AssetCategoryResponseDTO response = new AssetCategoryResponseDTO( 1L, "Books", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);

        when(assetCategoryService.getAssetCategoryById(1L)).thenReturn(response);

        ResponseEntity<AssetCategoryResponseDTO> result = assetCategoryController.getById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** UPDATE */
    @Test
    void updateAssetCategory_returnsOkWithUpdatesdAssetCategory(){
        AssetCategoryUpdateRequestDTO request = new AssetCategoryUpdateRequestDTO("Book", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);
        AssetCategoryResponseDTO response = new AssetCategoryResponseDTO( 1L, "Books", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);

        when(assetCategoryService.updateAssetCategory(1L, request)).thenReturn(response);

        ResponseEntity<AssetCategoryResponseDTO> result = assetCategoryController.update(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** DELETE */
    @Test
    void deleteAssetCategory_returnsNoContent() {

        ResponseEntity<Void> result = assetCategoryController.delete(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(result.getBody()).isNull();
        verify(assetCategoryService).deleteAssetCategory(1L);
    }


}
