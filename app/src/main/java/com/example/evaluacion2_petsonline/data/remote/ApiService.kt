package com.example.evaluacion2_petsonline.data.remote

import com.example.evaluacion2_petsonline.domain.model.LoginRequest
import com.example.evaluacion2_petsonline.domain.model.LoginResponse
import com.example.evaluacion2_petsonline.domain.model.Producto // Importante
import com.example.evaluacion2_petsonline.domain.model.RegisterRequest
import com.example.evaluacion2_petsonline.domain.model.Servicio // Importante
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // Recuerda que habíamos cambiado esto a RegisterRequest en el paso anterior
    @POST("auth/register")
    suspend fun signup(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<Map<String, Any>>

    // --- NUEVOS ENDPOINTS PARA MARKETPLACE ---
    @GET("productos")
    suspend fun getProductos(): Response<List<Producto>>

    @GET("servicios")
    suspend fun getServicios(): Response<List<Servicio>>
}