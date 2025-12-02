package com.example.evaluacion2_petsonline.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.evaluacion2_petsonline.domain.model.Producto
import com.example.evaluacion2_petsonline.domain.model.Servicio
import com.example.evaluacion2_petsonline.viewmodel.MarketplaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    navController: NavController,
    viewModel: MarketplaceViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Filtrado local (por si quieres buscar en tiempo real)
    val filteredProductos = state.productos.filter {
        it.nombre.contains(state.searchQuery, ignoreCase = true)
    }
    val filteredServicios = state.servicios.filter {
        it.nombre.contains(state.searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Marketplace Veterinario") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // --- BUSCADOR ---
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchChanged(it) },
                label = { Text("Buscar producto o servicio...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // --- FILTROS (CHIPS) ---
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.onTabSelected(0) },
                    label = { Text("Todos") }
                )
                FilterChip(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) },
                    label = { Text("Productos") }
                )
                FilterChip(
                    selected = state.selectedTab == 2,
                    onClick = { viewModel.onTabSelected(2) },
                    label = { Text("Servicios") }
                )
            }

            Spacer(Modifier.height(16.dp))

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    // SECCIÓN PRODUCTOS
                    if (state.selectedTab == 0 || state.selectedTab == 1) {
                        if (filteredProductos.isNotEmpty()) {
                            item { Text("Productos", style = MaterialTheme.typography.titleMedium) }
                            items(filteredProductos) { producto ->
                                ItemCard(
                                    titulo = producto.nombre,
                                    desc = producto.descripcion,
                                    precio = "$ ${producto.precio}",
                                    imagenUrl = producto.imagen,
                                    icono = Icons.Default.ShoppingCart
                                )
                            }
                        }
                    }

                    // SECCIÓN SERVICIOS
                    if (state.selectedTab == 0 || state.selectedTab == 2) {
                        if (filteredServicios.isNotEmpty()) {
                            item { Text("Servicios", style = MaterialTheme.typography.titleMedium) }
                            items(filteredServicios) { servicio ->
                                ItemCard(
                                    titulo = servicio.nombre,
                                    desc = servicio.descripcion,
                                    precio = "$ ${servicio.precio}",
                                    imagenUrl = servicio.imagen,
                                    icono = Icons.Default.Star
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Tarjeta Reutilizable para que se vea bonito
@Composable
fun ItemCard(titulo: String, desc: String, precio: String, imagenUrl: String, icono: ImageVector) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen
            AsyncImage(
                model = imagenUrl,
                contentDescription = null,
                modifier = Modifier.width(100.dp).fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            // Textos
            Column(
                modifier = Modifier.weight(1f).padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                Text(desc, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                Text(precio, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            }

            // Icono
            Icon(icono, contentDescription = null, modifier = Modifier.padding(16.dp))
        }
    }
}