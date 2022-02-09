package com.example.turnierplaner.tournament.invites

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.media.Image
import android.widget.EditText
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toAdaptiveIcon
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.drawable.toIcon
import androidx.navigation.NavController
import com.example.turnierplaner.R
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.ui.theme.Aquamarine
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import androidx.annotation.ColorInt
import androidx.core.graphics.toColorInt


@Composable
fun inviteScreen(navController: NavController, tournamentName: String?){

    val result = remember { mutableStateOf("") }
    val tourney = findTournament(tournamentName)

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title = { Text(text = tourney.name) },
                    actions = {
                        IconButton(
                            onClick = { navController.navigate("single_tournament_route/${tourney.name}") },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Button to go back to homescreen",
                            )
                        }
                    })
            }
        },
        content = {

            val bitmap = getQrCodeBitmap(tourney.inviteCode, tourney.name)
            val painter = BitmapPainter(bitmap.asImageBitmap())

            Image(
                painter = painter,
                contentDescription = "some useful description",
            )

            Box(
                Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "Invite Code: ${tourney.inviteCode}",
                    fontSize = 22.sp,
                    // fontFamily = FontFamily.Serif,
                    modifier = Modifier.align(Alignment.Center))
            }
        })

}

fun getQrCodeBitmap(inviteCode: Int?, tournamentName: String?): Bitmap {

    val size = 150 //pixels
    val qrCodeContent = "$inviteCode/$tournamentName"
    val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 } // Make the QR code buffer border narrower
    val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size)

    val white = "#ffffff"
    val black = "#000000"
    val whiteInt: Int = white.toColorInt()
    val blackInt: Int = black.toColorInt()

    return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
        for (x in 0 until size) {
            for (y in 0 until size) {
                it.setPixel(x, y, if (bits[x, y]) blackInt else whiteInt)
            }
        }
    }
}