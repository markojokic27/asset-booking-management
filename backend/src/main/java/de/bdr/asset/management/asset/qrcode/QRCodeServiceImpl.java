package de.bdr.asset.management.asset.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import de.bdr.asset.management.asset.dtos.AssetResponseDTO;
import de.bdr.asset.management.asset.AssetService;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QRCodeServiceImpl implements QRCodeService {
    @Value("${spring.application.qr.directory}")
    private String qrDirectory;

    private final AssetService assetService;

    public QRCodeServiceImpl(AssetService assetService) {
        this.assetService = assetService;
    }

    /*
        Function that gets the asset QR Code.

        If it exists, return the code string that is the filepath to the code.
        If not, generate and save it to the folder and update asset
    */
    @Override
    public String getQRCode(Long id)
        throws WriterException, IOException, ResourceNotFoundException {
        AssetResponseDTO assetDTO = assetService.getAssetById(id);

        if (assetDTO.code() != null && new File(assetDTO.code()).exists()) {
            log.info("QR Code exists and was found for asset {}", assetDTO.id());
            return assetDTO.code();
        }

        return generateAndSaveQRCode(id, assetDTO);
    }


    @Override
    public String generateAndSaveQRCode(Long id, AssetResponseDTO asset)
        throws WriterException, IOException, ResourceNotFoundException {

        File dir = new File(qrDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = qrDirectory + "/asset-" + id + ".png";

        // Variable to change what is added to the QR Code
        String content = asset.id().toString();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400);

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        File outputFile = new File(filePath);
        ImageIO.write(qrImage, "PNG", outputFile);

        assetService.updateAssetQRCode(id, filePath);

        return filePath;
    }
}
