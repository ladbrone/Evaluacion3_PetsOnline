package com.example.evaluacion2_petsonline.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.evaluacion2_petsonline.viewmodel.ReservaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaScreen(navController: NavController, vm: ReservaViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedServicioId by remember { mutableStateOf("") }
    var selectedServicioNombre by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearMessages()
        }
    }
    LaunchedEffect(state.success) {
        state.success?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearMessages()
            fecha = ""
            hora = ""
            selectedServicioNombre = ""
            selectedServicioId = ""
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Reservar Hora") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Nueva Reserva", style = MaterialTheme.typography.titleMedium)

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedServicioNombre,
                    onValueChange = {},
                    label = { Text("Selecciona un Servicio") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            Modifier.clickable { expanded = !expanded }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    if (state.listaServicios.isEmpty()) {
                        DropdownMenuItem(text = { Text("Cargando servicios...") }, onClick = {})
                    } else {
                        state.listaServicios.forEach { servicio ->
                            DropdownMenuItem(
                                text = { Text(servicio.nombre) },
                                onClick = {
                                    selectedServicioNombre = servicio.nombre
                                    selectedServicioId = servicio.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (AAAA-MM-DD)") },
                placeholder = { Text("Ej: 2025-12-25") },
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (HH:MM)") },
                placeholder = { Text("Ej: 15:30") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = { vm.crearReserva(selectedServicioId, fecha, hora) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && selectedServicioId.isNotEmpty()
            ) {
                if (state.isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                else Text("Confirmar Reserva")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Mis Reservas", style = MaterialTheme.typography.titleMedium)

            if (state.listaReservas.isEmpty()) {
                Text("No tienes reservas agendadas", style = MaterialTheme.typography.bodySmall)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(state.listaReservas) { reserva ->
                        Card(elevation = CardDefaults.cardElevation(2.dp)) {
                            Row(
                                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Fecha: ${reserva.fecha} - ${reserva.hora}")
                                    Text("Estado: ${reserva.estado}", style = MaterialTheme.typography.labelSmall)
                                }
                                IconButton(onClick = { vm.eliminarReserva(reserva.id) }) {
                                    Text("🗑️")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}