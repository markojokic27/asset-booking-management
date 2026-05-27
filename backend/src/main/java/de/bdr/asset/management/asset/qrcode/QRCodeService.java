package de.bdr.asset.management.asset.qrcode;

import java.io.IOException;

import com.google.zxing.WriterException;

import de.bdr.asset.management.asset.dtos.AssetResponseDTO;
import de.bdr.asset.management.core.exception.ResourceNotFoundException;

public interface QRCodeService {
    public String getQRCode(Long id) throws WriterException, IOException, ResourceNotFoundException;

    public String generateAndSaveQRCode(Long id, AssetResponseDTO asset) throws WriterException, IOException, ResourceNotFoundException;
}
