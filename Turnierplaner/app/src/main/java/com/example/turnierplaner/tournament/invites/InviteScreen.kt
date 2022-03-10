package com.example.turnierplaner.tournament.invites

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter


@Composable
fun inviteScreen(navController: NavController, tournamentName: String?){

    val result = remember { mutableStateOf("") }
    val tourney = findTournament(tournamentName)

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                //val encodedContent = AESEncryption.encrypt("${tourney.inviteCode}/$tournamentName")
                val bitmap = getQrCodeBitmap("${tourney.inviteCode}/$tournamentName")

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "qrcodepicture",
                    modifier = Modifier.fillMaxWidth(),

                    )

                Box {
                    Text(
                        text = "Tournament Name: ${tourney.name} \n" +
                                "Invite Code: ${tourney.inviteCode} \n",
                        fontSize = 22.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
        })

}

