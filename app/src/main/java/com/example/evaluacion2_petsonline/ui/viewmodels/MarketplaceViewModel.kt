package com.example.evaluacion2_petsonline.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.data.remote.RetrofitClient
import com.example.evaluacion2_petsonline.domain.model.Producto
import com.example.evaluacion2_petsonline.domain.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la pantalla Marketplace
data class MarketplaceUiState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val servicios: List<Servicio> = emptyList(),
    val selectedTab: Int = 0, // 0: Todos, 1: Productos, 2: Servicios
    val searchQuery: String = ""
)

class MarketplaceViewModel : ViewModel() {
    // Conexión directa a la API (o puedes usar un Repository si prefieres ser estricto)
    private val api = RetrofitClient.apiService

    private val _uiState = MutableStateFlow(MarketplaceUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchData()
    }

    // Descarga productos y servicios al iniciar
    private fun fetchData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Llamadas paralelas a la API
                val resProductos = api.getProductos()
                val resServicios = api.getServicios()

                if (resProductos.isSuccessful && resServicios.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            productos = resProductos.body() ?: emptyList(),
                            servicios = resServicios.body() ?: emptyList()
                        )
                    }
                } else {
                    println("Error cargando marketplace: ${resProductos.code()}")
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                println("Error de conexión: ${e.message}")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // Funciones para que la UI controle el filtro y buscador
    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun onSearchChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}