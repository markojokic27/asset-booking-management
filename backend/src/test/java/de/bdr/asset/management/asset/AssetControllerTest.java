package de.bdr.asset.management.asset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssetControllerTest {
    @Mock
    private AssetService assetService;

    @InjectMocks
    private AssetController assetController;

    /** CREATE */
    @Test
    void createAsset_validRequest_returnsCreatedStatus(){
        AssetRequestDTO request = new AssetRequestDTO("Hp 15", 1L, "Laptop located in room 301", "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "Room 301");
        AssetResponseDTO response = new AssetResponseDTO(1L, "Hp 15", 1L, "Laptop located in room 301", "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "Room 301");

        when(assetService.createAsset(request)).thenReturn(response);

        ResponseEntity<AssetResponseDTO> result = assetController.createAsset(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        verify(assetService).createAsset(request);

    }

    /** READ ALL */
    @Test
    void getAllAssets_returnsOkWithLIst(){
        AssetResponseDTO response = new AssetResponseDTO(1L,
                "Hp 15",
                1L,
                "Laptop located in room 301",
                "QR-LAPTOP-001",
                AssetStatusEnum.ACTIVE,
                "Room 301");

        List<AssetResponseDTO> list = List.of(response);
        Page<AssetResponseDTO> page = new PageImpl<>(list);

        when(assetService.getAllAssets(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<AssetResponseDTO>> result =
                assetController.getAllAssets(PageRequest.of(0, 10));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert(result.getBody() != null);
        assertThat(result.getBody().getContent())
                .hasSize(1)
                .contains(response);
    }

    /** READ BY ID */
    @Test
    void getAssetById_returnsOkWithAsset(){
        AssetResponseDTO response = new AssetResponseDTO(1L, "Hp 15", 1L, "Laptop located in room 301", "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "Room 301");

        when(assetService.getAssetById(1L)).thenReturn(response);

        ResponseEntity<AssetResponseDTO> result = assetController.getAssetById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** UPDATE */
    @Test
    void updateAssetById_returnsOkWithUpdatesdAsset(){
        AssetRequestDTO request = new AssetRequestDTO("Hp 15", 1L, "Laptop located in room 301", "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "Room 301");
        AssetResponseDTO response = new AssetResponseDTO(1L, "Hp 15", 1L, "Laptop located in room 301", "QR-LAPTOP-001", AssetStatusEnum.ACTIVE, "Room 301");

        when(assetService.updateAsset(1L, request)).thenReturn(response);

        ResponseEntity<AssetResponseDTO> result = assetController.updateAsset(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }



}
