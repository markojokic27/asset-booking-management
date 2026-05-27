package de.bdr.asset.management.asset.qr;

import de.bdr.asset.management.asset.dtos.AssetResponseDTO;
import de.bdr.asset.management.asset.AssetService;
import de.bdr.asset.management.asset.AssetStatusEnum;
import de.bdr.asset.management.asset.qrcode.QRCodeServiceImpl;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QRCodeServiceImplTest {

    @Mock
    private AssetService assetService;

    @InjectMocks
    private QRCodeServiceImpl qrCodeService;

    @TempDir
    Path tempDir;

    private AssetResponseDTO assetDTO;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(qrCodeService, "qrDirectory", tempDir.toString());

        assetDTO = new AssetResponseDTO(
                1L,
                "Laptop Dell",
                1L,
                "A high-performance laptop",
                null,
                AssetStatusEnum.ACTIVE,
                "Office A"
        );
    }

    // --- getQRCode ---

    @Test
    void shouldGenerateQRCodeWhenCodeIsNull() throws Exception {
        when(assetService.getAssetById(1L)).thenReturn(assetDTO);

        String result = qrCodeService.getQRCode(1L);

        assertThat(result).endsWith("asset-1.png");
        assertThat(new File(result)).exists();
        verify(assetService).updateAssetQRCode(1L, result);
    }

    @Test
    void shouldReturnExistingQRCodeWhenFileExists() throws Exception {
        File existingFile = tempDir.resolve("asset-1.png").toFile();
        boolean created = existingFile.createNewFile();
        assertThat(created).isTrue();

        AssetResponseDTO assetWithCode = new AssetResponseDTO(
                1L,
                "Laptop Dell",
                1L,
                "A high-performance laptop",
                existingFile.getAbsolutePath(),
                AssetStatusEnum.ACTIVE,
                "Office A"
        );

        when(assetService.getAssetById(1L)).thenReturn(assetWithCode);

        String result = qrCodeService.getQRCode(1L);

        assertThat(result).isEqualTo(existingFile.getAbsolutePath());
        verify(assetService, never()).updateAssetQRCode(any(), any());
    }

    @Test
    void shouldGenerateQRCodeWhenCodeIsNotNullButFileDoesNotExist() throws Exception {
        AssetResponseDTO assetWithNonExistentCode = new AssetResponseDTO(
                1L,
                "Laptop Dell",
                1L,
                "A high-performance laptop",
                "/non/existent/path/asset-1.png",
                AssetStatusEnum.ACTIVE,
                "Office A"
        );

        when(assetService.getAssetById(1L)).thenReturn(assetWithNonExistentCode);

        String result = qrCodeService.getQRCode(1L);

        assertThat(result).endsWith("asset-1.png");
        assertThat(new File(result)).exists();
        verify(assetService).updateAssetQRCode(1L, result);
    }

    @Test
    void shouldThrowExceptionWhenAssetNotFound() {
        when(assetService.getAssetById(1L)).thenThrow(new ResourceNotFoundException("Asset not found with id: 1"));

        assertThrows(ResourceNotFoundException.class,
                () -> qrCodeService.getQRCode(1L));

        verify(assetService, never()).updateAssetQRCode(any(), any());
    }

    // --- generateAndSaveQRCode ---

    @Test
    void shouldGenerateAndSaveQRCodeFile() throws Exception {
        String result = qrCodeService.generateAndSaveQRCode(1L, assetDTO);

        assertThat(result).endsWith("asset-1.png");
        assertThat(new File(result)).exists();
    }

    @Test
    void shouldCallUpdateAssetQRCodeAfterGeneration() throws Exception {
        String result = qrCodeService.generateAndSaveQRCode(1L, assetDTO);

        verify(assetService).updateAssetQRCode(1L, result);
    }

    @Test
    void shouldCreateQRDirectoryIfNotExists() throws Exception {
        Path subDir = tempDir.resolve("new-qr-dir");
        ReflectionTestUtils.setField(qrCodeService, "qrDirectory", subDir.toString());

        String result = qrCodeService.generateAndSaveQRCode(1L, assetDTO);

        assertThat(new File(subDir.toString())).exists();
        assertThat(new File(result)).exists();
    }

    @Test
    void shouldGenerateQRCodeWithCorrectFileName() throws Exception {
        String result = qrCodeService.generateAndSaveQRCode(5L, new AssetResponseDTO(
                5L, "Monitor", 1L, null, null, AssetStatusEnum.ACTIVE, "Office B"
        ));

        assertThat(result).contains("asset-5.png");
    }
}