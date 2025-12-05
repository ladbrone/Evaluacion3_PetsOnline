package com.example.evaluacion2_petsonline.data.remote

import com.example.evaluacion2_petsonline.domain.model.LoginRequest
import com.example.evaluacion2_petsonline.domain.model.LoginResponse
import com.example.evaluacion2_petsonline.domain.model.Producto
import com.example.evaluacion2_petsonline.domain.model.RegisterRequest
import com.example.evaluacion2_petsonline.domain.model.Servicio
import com.example.evaluacion2_petsonline.domain.model.Mascota
import com.example.evaluacion2_petsonline.domain.model.Reserva
import com.example.evaluacion2_petsonline.domain.model.ReservaRequest
import com.example.evaluacion2_petsonline.domain.model.ReservaResponse

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun signup(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<Map<String, Any>>

    @GET("productos")
    suspend fun getProductos(): Response<List<Producto>>

    @GET("servicios")
    suspend fun getServicios(): Response<List<Servicio>>

    @GET("mascotas")
    suspend fun getMascotas(@Header("Authorization") token: String): Response<List<Mascota>>


    @DELETE("mascotas/{id}")
    suspend fun deleteMascota(@Header("Authorization") token: String, @Path("id") id: String): Response<Unit>

    @GET("reservas")
    suspend fun getMisReservas(@Header("Authorization") token: String): Response<List<Reserva>>

    @POST("reservas")
    suspend fun crearReserva(
        @Header("Authorization") token: String,
        @Body request: ReservaRequest
    ): Response<ReservaResponse>

    @DELETE("reservas/{id}")
    suspend fun eliminarReserva(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>
}