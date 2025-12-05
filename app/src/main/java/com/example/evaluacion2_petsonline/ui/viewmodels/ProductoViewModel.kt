package com.example.evaluacion2_petsonline.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.data.repository.ProductoRepository
import com.example.evaluacion2_petsonline.domain.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ProductoRepository()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            val resultado = repo.getProductos()

            resultado.onSuccess { lista ->
                _productos.value = lista
            }.onFailure {
                println("Error cargando productos: ${it.message}")
            }
        }
    }
}