package com.example.turnierplaner.tournament.invites

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

fun getQrCodeBitmap(encodedString: String?): Bitmap {

    val size = 1000 //pixels
    val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 } // Make the QR code buffer border narrower
    val bits = QRCodeWriter().encode(encodedString, BarcodeFormat.QR_CODE, size, size)

    val white = Color.White.toArgb()
    val black = Color.Black.toArgb()

    return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
        for (x in 0 until size) {
            for (y in 0 until size) {
                it.setPixel(x, y, if (bits[x, y]) black else white)
            }
        }
    }
}