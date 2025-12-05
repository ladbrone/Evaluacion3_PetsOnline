package com.example.evaluacion2_petsonline.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.evaluacion2_petsonline.utils.NotificationHelper
import com.example.evaluacion2_petsonline.ui.viewmodels.HomeViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    val viewModel: HomeViewModel = viewModel()

    var permisoConcedido by remember { mutableStateOf(false) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { permitido -> permisoConcedido = permitido }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permisoConcedido = true
        } else {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(permisoConcedido) {
        if (permisoConcedido) {
            NotificationHelper.showNotification(
                context,
                "PetsOnline 🐾",
                "¡Bienvenido! Revisa las novedades en nuestra tienda."
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Pantalla Home",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Mascota Destacada del día 🐶",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                AsyncImage(
                    model = viewModel.dogImageState,
                    contentDescription = "Foto aleatoria de perro",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { navController.navigate("profile") },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Ver Perfil")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("mascotas") },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Mis Mascotas")
            }

            Spacer(Modifier.height(12.dp))


            Button(
                onClick = { navController.navigate("marketplace") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Ir al Marketplace (Tienda y Servicios)")
            }


            Spacer(Modifier.height(12.dp))


            Button(
                onClick = { navController.navigate("reserva") },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Reservar Cita")
            }
        }
    }
}