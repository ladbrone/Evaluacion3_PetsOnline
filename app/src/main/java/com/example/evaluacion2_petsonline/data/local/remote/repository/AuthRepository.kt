package com.example.evaluacion2_petsonline.repository

import android.content.Context
import com.example.evaluacion2_petsonline.data.local.SessionManager
import com.example.evaluacion2_petsonline.data.remote.ApiService
import com.example.evaluacion2_petsonline.data.remote.LoginRequest
import com.example.evaluacion2_petsonline.data.remote.RetrofitClient

class AuthRepository(context: Context) {
    private val api = RetrofitClient.create(context).create(ApiService::class.java)
    private val session = SessionManager(context)

    // 🔹 LOGIN
    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(email, password))
            val token = response.authToken ?: return Result.failure(Exception("No se recibió token"))
            session.saveToken(token)
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 🔹 SIGNUP
    suspend fun signup(email: String, password: String): Result<String> {
        return try {
            val response = api.signup(LoginRequest(email, password))
            val token = response.authToken ?: return Result.failure(Exception("No se recibió token"))
            session.saveToken(token)
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 🔹 PERFIL (opcional, muestra email)
    suspend fun getProfile(): Result<String> {
        return try {
            val token = session.getToken() ?: return Result.failure(Exception("No hay token guardado"))
            val profile = api.getProfile("Bearer $token")
            Result.success(profile.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 🔹 LOGOUT
    suspend fun logout() {
        session.clearToken()
    }
}
