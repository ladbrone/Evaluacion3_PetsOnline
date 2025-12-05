package com.example.evaluacion2_petsonline.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _uiState = MutableStateFlow(MarketplaceUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarDatosManuales()
    }

    private fun cargarDatosManuales() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val listaProductos = listOf(
                Producto(
                    id = "1",
                    nombre = "Alimento Premium Perro",
                    descripcion = "Saco de 15kg sabor carne y arroz.",
                    precio = 45000,
                    imagen = "https://images.pexels.com/photos/59523/pexels-photo-59523.jpeg", // URL de ejemplo
                    stock = 10,
                    categoria = "Alimentos"
                ),
                Producto(
                    id = "2",
                    nombre = "Juguete Hueso Goma",
                    descripcion = "Resistente a mordidas fuertes.",
                    precio = 5990,
                    imagen = "",
                    stock = 50,
                    categoria = "Juguetes"
                ),
                Producto(
                    id = "3",
                    nombre = "Collar Antipulgas",
                    descripcion = "Protección efectiva por 8 meses.",
                    precio = 12990,
                    imagen = "",
                    stock = 20,
                    categoria = "Salud"
                ),
                Producto(
                    id = "4",
                    nombre = "Cama Acolchada L",
                    descripcion = "Cama suave para razas grandes.",
                    precio = 25000,
                    imagen = "",
                    stock = 5,
                    categoria = "Accesorios"
                )
            )

            val listaServicios = listOf(
                Servicio(
                    id = "692ed9570c37c914c54fa07e",
                    nombre = "Consulta General",
                    descripcion = "Revisión completa de salud.",
                    precio = 20000,
                    imagen = "",
                    duracion = "30 min"
                ),
                Servicio(
                    id = "692ed9610c37c914c54fa07f",
                    nombre = "Baño y Corte",
                    descripcion = "Estética canina completa.",
                    precio = 35000,
                    imagen = "",
                    duracion = "60 min"
                ),
                Servicio(
                    id = "serv_3",
                    nombre = "Vacunación Anual",
                    descripcion = "Sextuple + Antirrábica.",
                    precio = 18000,
                    imagen = "",
                    duracion = "15 min"
                )
            )

            kotlinx.coroutines.delay(500)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    productos = listaProductos,
                    servicios = listaServicios
                )
            }
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun onSearchChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}