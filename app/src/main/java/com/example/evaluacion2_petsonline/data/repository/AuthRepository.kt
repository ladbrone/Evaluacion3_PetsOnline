package com.example.evaluacion2_petsonline.data.repository

import android.content.Context
import com.example.evaluacion2_petsonline.data.local.SessionManager
import com.example.evaluacion2_petsonline.data.remote.RetrofitClient
import com.example.evaluacion2_petsonline.domain.model.LoginRequest
import com.example.evaluacion2_petsonline.domain.model.LoginResponse

class AuthRepository(context: Context) {

    private val apiService = RetrofitClient.apiService
    private val sessionManager = SessionManager(context)

    suspend fun login(email: String, pass: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email = email, password = pass)

            // Llamada a la API
            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!

                // Guardamos el token en SessionManager
                loginResponse.accessToken?.let { token ->
                    sessionManager.saveToken(token)
                }

                Result.success(loginResponse)
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Credenciales incorrectas"
                    404 -> "Usuario no encontrado"
                    else -> "Error del servidor: ${response.code()}"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.localizedMessage}"))
        }
    }

    // 3. ¡IMPORTANTE! Agregamos 'suspend' aquí para corregir el error rojo
    suspend fun getToken(): String? {
        return sessionManager.getToken()
    }

    // 4. ¡IMPORTANTE! Agregamos 'suspend' aquí también
    suspend fun logout() {
        sessionManager.clearSession()
    }
}