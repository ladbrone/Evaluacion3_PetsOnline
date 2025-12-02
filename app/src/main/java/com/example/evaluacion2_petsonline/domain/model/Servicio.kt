package com.example.evaluacion2_petsonline.domain.model

import com.google.gson.annotations.SerializedName

data class Servicio(
    @SerializedName("_id") val id: String, // MongoDB usa String
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val imagen: String, // Agregamos imagen
    val duracion: String
)