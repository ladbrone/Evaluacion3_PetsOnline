package com.example.evaluacion2_petsonline.data.repository

import android.content.Context
import com.example.evaluacion2_petsonline.data.local.SessionManager
import com.example.evaluacion2_petsonline.data.remote.RetrofitClient
import com.example.evaluacion2_petsonline.domain.model.Reserva
import com.example.evaluacion2_petsonline.domain.model.ReservaRequest
import com.example.evaluacion2_petsonline.domain.model.ReservaResponse
import com.example.evaluacion2_petsonline.domain.model.Servicio

class ReservaRepository(context: Context) {
    private val api = RetrofitClient.apiService
    private val sessionManager = SessionManager(context)

    suspend fun getServicios(): Result<List<Servicio>> {
        val serviciosFijos = listOf(
            Servicio(
                id = "692ed9570c37c914c54fa07e",
                nombre = "Consulta General",
                descripcion = "Revisión completa de salud y signos vitales.",
                precio = 20000,
                imagen = "",
                duracion = "30 min"
            ),
            Servicio(
                id = "692ed9610c37c914c54fa07f",
                nombre = "Baño y Corte",
                descripcion = "Baño sanitario, corte de uñas y limpieza.",
                precio = 35000,
                imagen = "",
                duracion = "60 min"
            ),
            Servicio(
                id = "dummy_vacunacion",
                nombre = "Vacunación",
                descripcion = "Aplicación de vacunas anuales.",
                precio = 15000,
                imagen = "",
                duracion = "15 min"
            )
        )
        return Result.success(serviciosFijos)
    }


    suspend fun getReservas(): Result<List<Reserva>> {
        val token = sessionManager.getToken() ?: return Result.failure(Exception("Sin token"))
        return try {
            val response = api.getMisReservas("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearReserva(servicioId: String, fecha: String, hora: String): Result<ReservaResponse> {
        val token = sessionManager.getToken() ?: return Result.failure(Exception("Sin token"))
        return try {
            val request = ReservaRequest(servicioId, fecha, hora)
            val response = api.crearReserva("Bearer $token", request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al reservar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarReserva(id: String): Result<Unit> {
        val token = sessionManager.getToken() ?: return Result.failure(Exception("Sin token"))
        return try {
            val response = api.eliminarReserva("Bearer $token", id)
            if (response.isSuccessful) Result.success(Unit) else Result.failure(Exception("Error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}