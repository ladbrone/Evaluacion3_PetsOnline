package com.example.evaluacion2_petsonline.domain.model

import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("_id") val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val imagen: String,
    val stock: Int,
    val categoria: String
)