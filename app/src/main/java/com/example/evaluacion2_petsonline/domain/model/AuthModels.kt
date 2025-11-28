package com.example.evaluacion2_petsonline.domain.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val user: User,
    @SerializedName(value = "access_token", alternate = ["token", "authToken", "accessToken"])
    val accessToken: String?
)

data class User(
    @SerializedName("_id") val id: String,
    val email: String,
    val role: String,
    val isActive: Boolean,
    val emailVerified: Boolean
)