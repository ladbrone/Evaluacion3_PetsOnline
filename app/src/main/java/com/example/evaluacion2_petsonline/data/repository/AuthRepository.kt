package com.example.evaluacion2_petsonline.data.repository

import android.content.Context
import com.example.evaluacion2_petsonline.data.local.SessionManager
import com.example.evaluacion2_petsonline.data.remote.RetrofitClient
import com.example.evaluacion2_petsonline.domain.model.LoginRequest
import com.example.evaluacion2_petsonline.domain.model.LoginResponse
import com.example.evaluacion2_petsonline.domain.model.RegisterRequest // 👈 IMPORTANTE: Agregamos este import

class AuthRepository(context: Context) {

    private val apiService = RetrofitClient.apiService
    private val sessionManager = SessionManager(context)

    suspend fun login(email: String, pass: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email = email, password = pass)
            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!

                loginResponse.data?.accessToken?.let { token ->
                    sessionManager.saveToken(token)
                    println("🔑 Login: Token guardado correctamente")
                } ?: run {
                    println("⚠️ Login: No se encontró el token en la respuesta")
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

    suspend fun signup(email: String, pass: String): Result<LoginResponse> {
        return try {
            val request = RegisterRequest(
                email = email,
                password = pass,
                fullName = "Usuario App",
                role = "ADMIN"
            )

            val response = apiService.signup(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!

                loginResponse.data?.accessToken?.let { token ->
                    sessionManager.saveToken(token)
                    println("🔑 Registro: Token guardado correctamente")
                }

                Result.success(loginResponse)
            } else {
                val errorMsg = when (response.code()) {
                    400 -> "Datos inválidos (Contraseña débil o usuario ya existe)"
                    else -> "Error en registro: ${response.code()}"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.localizedMessage}"))
        }
    }

    suspend fun getProfile(): Result<String> {
        return try {
            val token = sessionManager.getToken()

            if (token == null) {
                return Result.failure(Exception("No hay token guardado"))
            }

            val response = apiService.getProfile("Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!

                println("🔍 JSON Perfil Completo: $body")

                val email = try {
                    val dataMap = body["data"] as? Map<*, *>
                    val userMap = dataMap?.get("user") as? Map<*, *>

                    if (userMap != null && userMap.containsKey("email")) {
                        userMap["email"]?.toString()
                    } else if (dataMap != null && dataMap.containsKey("email")) {
                        dataMap["email"]?.toString()
                    } else {
                        body["email"]?.toString()
                    }
                } catch (e: Exception) {
                    null
                }

                Result.success(email ?: "Correo no encontrado en la estructura")

            } else {
                Result.failure(Exception("Error al obtener perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error: ${e.localizedMessage}"))
        }
    }

    suspend fun getToken(): String? {
        return sessionManager.getToken()
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }
}