package de.bdr.asset.management.asset.qrcode;

import java.io.IOException;

import com.google.zxing.WriterException;

public interface QRCodeService {
    static final String QR_DIRECTORY = "qrcodes";

    public String generateAndSaveQRCodeForAsset(Long id) throws WriterException, IOException ;
}
