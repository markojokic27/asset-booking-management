package com.example.assetbookingmanagement.features.asset.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.R
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun QrScannerButton(
    modifier: Modifier = Modifier,
    onQrScanned: (String) -> Unit
) {
    // Gets the current context to initialize the QR code scanner
    val context = LocalContext.current

    val scanner = remember {
        // Sets the scanner to only recognize QR codes
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        //Creates the ML Kit QR scanner
        GmsBarcodeScanning.getClient(context, options)
    }

    Box(
        modifier = modifier
            .padding(end = 20.dp, bottom = 24.dp)
            .size(64.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            .clickable {
                scanner.startScan()
                    .addOnSuccessListener { barcode ->
                        val scannedValue = barcode.rawValue
                        if (!scannedValue.isNullOrBlank()) {
                            onQrScanned(scannedValue)
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.qr_scanner_error_empty_code),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        }
                        .addOnCanceledListener {
                            Toast.makeText(
                                context,
                                context.getString(R.string.qr_scanner_cancelled),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                context.getString(R.string.qr_scanner_error_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.QrCodeScanner,
            contentDescription = stringResource(R.string.qr_scanner_action),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(30.dp)
        )
    }
}
