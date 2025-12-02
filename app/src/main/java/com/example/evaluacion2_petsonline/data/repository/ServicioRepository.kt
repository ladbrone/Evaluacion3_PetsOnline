package com.example.evaluacion2_petsonline.data.repository

import com.example.evaluacion2_petsonline.data.remote.RetrofitClient
import com.example.evaluacion2_petsonline.domain.model.Servicio

class ServicioRepository {
    // Instancia de la API
    private val apiService = RetrofitClient.apiService

    // Función para obtener servicios desde la Nube (Render)
    suspend fun getServicios(): Result<List<Servicio>> {
        return try {
            val response = apiService.getServicios()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cargar servicios: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}