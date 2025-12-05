package com.example.evaluacion2_petsonline.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.data.repository.ReservaRepository
import com.example.evaluacion2_petsonline.domain.model.Reserva
import com.example.evaluacion2_petsonline.domain.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReservaUiState(
    val listaReservas: List<Reserva> = emptyList(),
    val listaServicios: List<Servicio> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null
)

class ReservaViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ReservaRepository(app)
    private val _uiState = MutableStateFlow(ReservaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarDatosIniciales()
    }

    private fun cargarDatosIniciales() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repo.getServicios().onSuccess { servicios ->
                _uiState.update { it.copy(listaServicios = servicios) }
            }

            cargarReservas()
        }
    }

    fun cargarReservas() {
        viewModelScope.launch {
            val result = repo.getReservas()
            result.onSuccess { reservas ->
                _uiState.update { it.copy(isLoading = false, listaReservas = reservas) }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun crearReserva(servicioId: String, fecha: String, hora: String) {
        if (servicioId.isBlank() || fecha.isBlank() || hora.isBlank()) {
            _uiState.update { it.copy(error = "Faltan campos por completar") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }

            val result = repo.crearReserva(servicioId, fecha, hora)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, success = "¡Reserva Creada!") }
                cargarReservas()
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = "Error: ${e.message}") }
            }
        }
    }

    fun eliminarReserva(id: String) {
        viewModelScope.launch {
            repo.eliminarReserva(id)
            cargarReservas()
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, success = null) }
    }
}