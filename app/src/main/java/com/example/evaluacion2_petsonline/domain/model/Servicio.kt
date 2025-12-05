package com.example.evaluacion2_petsonline.domain.model

import com.google.gson.annotations.SerializedName

data class Servicio(
    @SerializedName("_id") val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val imagen: String,
    val duracion: String
)