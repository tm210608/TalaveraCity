package com.example.eboraazule.data.model

/**
 * Modelo de dominio para una pieza de cerámica o azulejo.
 */
data class Azulejo(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val imagenUrl: String,
    val tipo: String
)
