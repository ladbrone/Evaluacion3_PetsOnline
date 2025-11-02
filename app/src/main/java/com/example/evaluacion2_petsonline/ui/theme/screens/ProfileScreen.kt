package com.example.evaluacion2_petsonline.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.evaluacion2_petsonline.viewmodel.ProfileViewModel
import com.example.evaluacion2_petsonline.data.local.SessionManager
import com.example.evaluacion2_petsonline.utils.ImagePickerDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(navController: NavController, vm: ProfileViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()
    val session = remember { SessionManager(navController.context) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Perfil de Usuario", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            ImagePickerDialog(onImagePicked = { uri: Uri? ->
                uri?.let { vm.saveAvatar(it.toString()) } // Guarda la imagen en DataStore
            })

            Spacer(Modifier.height(16.dp))

            uiState.avatarUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(text = "Usuario logueado:")
            Text(
                text = uiState.email ?: "Sin correo",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(24.dp))

            Button(onClick = { navController.navigate("home") }) {
                Text("Volver al Home")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        session.clearToken()
                        withContext(Dispatchers.Main) {
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
