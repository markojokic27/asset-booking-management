package de.bdr.asset.management.asset.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import de.bdr.asset.management.asset.AssetRequestDTO;
import de.bdr.asset.management.asset.AssetResponseDTO;
import de.bdr.asset.management.asset.AssetService;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QRCodeServiceImpl implements QRCodeService {
    private final AssetService assetService;

    public QRCodeServiceImpl(AssetService assetService) {
        this.assetService = assetService;
    }

    @Override
    public String generateAndSaveQRCodeForAsset(Long id) throws WriterException, IOException, ResourceNotFoundException {

        // Checks to see if the QR_DIRECTORY (qrcodes) exists. If not, create it
        File dir = new File(QR_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Get the asset with ID and filePath that is stored in the code column
        AssetResponseDTO asset = assetService.getAssetById(id);
        String filePath = asset.code();
        
        if (filePath == null || !new File(filePath).exists()) {
            // Set the new filePath if it does not exist or is not stored
            filePath = QR_DIRECTORY + "/asset-" + id + ".png";

            // Since the service returns DTOs, need to make a new DTO
            // with the updated code field and use the service method later to update it.
            AssetRequestDTO updatedAsset = new AssetRequestDTO(
                asset.name(),
                asset.categoryId(), 
                asset.description(), 
                filePath, 
                asset.status(), 
                asset.location()
            );
            
            // Variable that holds the QR code content.
            // TODO: See what is the standard for QR codes to hold to make it easy to use in mobile app
            String content = asset.id().toString();
            
            // Code that creates the QR code and turns it into bytes that later get turned into an image
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400);
    
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
    
            File outputFile = new File(filePath);
            ImageIO.write(qrImage, "PNG", outputFile);
            
            log.info("Created QR Code for future use for asset with id: {}", id);

            // Finally, update the asset to hold the path to the QR code
            assetService.updateAsset(id, updatedAsset);
        }

        return filePath;
    }
}
