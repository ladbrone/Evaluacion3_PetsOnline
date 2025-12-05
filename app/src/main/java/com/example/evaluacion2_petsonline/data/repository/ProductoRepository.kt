package com.example.evaluacion2_petsonline.data.repository


import com.example.evaluacion2_petsonline.data.remote.RetrofitClient
import com.example.evaluacion2_petsonline.domain.model.Producto

class ProductoRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getProductos(): Result<List<Producto>> {
        return try {
            val response = apiService.getProductos()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cargar productos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}