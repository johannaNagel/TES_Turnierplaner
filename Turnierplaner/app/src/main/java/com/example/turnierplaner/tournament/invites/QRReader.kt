package com.example.turnierplaner.tournament.invites


import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme

import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.googlesignin.ui.login.showMessage
import com.example.turnierplaner.tournament.tournamentDB.getTournamentFromDB
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture
import java.lang.NumberFormatException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.jar.Manifest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun qrReaderScreen(navController: NavHostController){

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title = { Text(text = "QR-Reader") },
                    actions = {
                        IconButton(
                            onClick = { navController.navigate(BottomBarScreens.Home.route) },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Button to go back to homescreen",
                            )
                        }
                    }
                )
            }
        },
        content = {

            Surface(color = MaterialTheme.colors.background) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

                    Button(
                        onClick = {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    ) {
                        Text(text = "Camera Permission")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    CameraPreview(navController)
                }
            }
        })
}

@Composable
fun CameraPreview(navController: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    val qrCodeVal = remember { mutableStateOf("") }

    AndroidView(
        factory = { AndroidViewContext ->
            PreviewView(AndroidViewContext).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        update = { previewView ->
            val cameraSelector: CameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val barcodeAnalyser = QrCodeAnalyser { barcodes ->
                    barcodes.forEach { barcode ->
                        barcode.rawValue?.let { barcodeValue ->
                            qrCodeVal.value = barcodeValue

                            try {

                                if(!qrCodeVal.value.contains("/")){

                                    showMessage(context, message = "This Qr-Code does not lead to a valid tournament.")
                                }
                                else{
                                    val result = qrCodeVal.value
                                    val inviteTournamentName = result.split("/")[1]
                                    val inviteCode = result.split("/")[0]
                                    getTournamentFromDB(inviteTournamentName, navController , context, inviteCode.toInt())

                                }

                            }
                            catch (e: NumberFormatException){

                                showMessage(context, message = "This Qr-Code does not lead to a valid tournament.")

                            }

                            //Toast.makeText(context, inviteCode, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                    }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )
}





