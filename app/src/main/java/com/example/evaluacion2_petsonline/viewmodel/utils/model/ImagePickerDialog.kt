package com.example.evaluacion2_petsonline.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.evaluacion2_petsonline.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ImagePickerDialog(
    onImagePicked: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
        onImagePicked(uri)
    }

    val photoFile = remember {
        File.createTempFile(
            "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}",
            ".jpg",
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    }

    val cameraUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        photoFile
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = cameraUri
            onImagePicked(cameraUri)
        }
    }

    val cameraPermissionGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Avatar",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_menu_camera),
                    contentDescription = "Seleccionar imagen"
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Seleccionar imagen") },
                text = {
                    Column {
                        Button(onClick = {
                            if (cameraPermissionGranted) {
                                cameraLauncher.launch(cameraUri)
                                showDialog = false
                            } else {
                                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                                (context as? Activity)?.startActivity(intent)
                                showDialog = false
                            }
                        }) {
                            Text("📷 Tomar foto")
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(onClick = {
                            galleryLauncher.launch("image/*")
                            showDialog = false
                        }) {
                            Text("🖼️ Elegir desde galería")
                        }
                    }
                },
                confirmButton = {}
            )
        }
    }
}
