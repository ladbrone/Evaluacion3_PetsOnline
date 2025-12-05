package com.example.evaluacion2_petsonline.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.data.repository.ServicioRepository
import com.example.evaluacion2_petsonline.domain.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServicioViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ServicioRepository()

    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios

    init {
        cargarServicios()
    }

    private fun cargarServicios() {
        viewModelScope.launch {
            val resultado = repo.getServicios()

            resultado.onSuccess { lista ->
                _servicios.value = lista
            }.onFailure {
                println("Error cargando servicios: ${it.message}")
            }
        }
    }
}