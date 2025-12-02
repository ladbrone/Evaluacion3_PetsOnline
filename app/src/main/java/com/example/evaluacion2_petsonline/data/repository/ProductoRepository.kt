package com.example.evaluacion2_petsonline.data.repository

// Nota: Si el archivo está en la carpeta 'local', muévelo a 'repository'
// o ajusta la línea del package. Lo ideal es: data.repository

import com.example.evaluacion2_petsonline.data.remote.RetrofitClient
import com.example.evaluacion2_petsonline.domain.model.Producto

class ProductoRepository {
    // Instancia de la API
    private val apiService = RetrofitClient.apiService

    // Función para obtener productos desde la Nube (Render)
    suspend fun getProductos(): Result<List<Producto>> {
        return try {
            val response = apiService.getProductos()

            if (response.isSuccessful && response.body() != null) {
                // Éxito: Devolvemos la lista que viene del servidor
                Result.success(response.body()!!)
            } else {
                // Error del servidor (ej: 404, 500)
                Result.failure(Exception("Error al cargar productos: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Error de conexión
            Result.failure(e)
        }
    }
}