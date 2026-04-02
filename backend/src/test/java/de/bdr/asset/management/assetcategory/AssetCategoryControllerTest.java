package de.bdr.asset.management.assetcategory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    // TODO: Configure this for pagination
//    @Test
//    void getAllAssetCategories_returnsOkWithLIst(){
//        AssetCategoryResponseDTO response = new AssetCategoryResponseDTO( 1L, "Books", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);
//
//        List<AssetCategoryResponseDTO> list = List.of(response);
//        when(assetCategoryService.getAllAssetCategories()).thenReturn(list);
//
//        ResponseEntity<List<AssetCategoryResponseDTO>> result = assetCategoryController.getAll();
//
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(result.getBody()).hasSize(1).contains(response);
//    }

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
        AssetCategoryRequestDTO request = new AssetCategoryRequestDTO( "Book", "A collection of books available for borrowing within the company library.", BookingPeriodEnum.DAY, Boolean.TRUE);
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
