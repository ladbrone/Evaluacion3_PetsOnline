package com.example.evaluacion2_petsonline.domain.model

import com.google.gson.annotations.SerializedName

data class Reserva(
    @SerializedName("_id") val id: String,
    val fecha: String,
    val hora: String,
    val estado: String,
    val servicio: Any? = null
)

data class ReservaRequest(
    val servicioId: String,
    val fecha: String,
    val hora: String
)

data class ReservaResponse(
    val success: Boolean,
    val message: String
)