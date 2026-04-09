package de.bdr.asset.management.asset.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    @Override
    public String generateAndSaveQRCode(String text, Long assetId) throws WriterException, IOException {

        File dir = new File(QR_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = QR_DIRECTORY + "/asset-" + assetId + ".png";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 400, 400);

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        File outputFile = new File(filePath);
        ImageIO.write(qrImage, "PNG", outputFile);

        return filePath;
    }

    @Override
    public byte[] loadQRCode(String filePath) throws IOException {
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
