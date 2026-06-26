package com.talaveracity.app.domain.model

enum class PoiCategory {
    MONUMENT,
    MUSEUM,
    CHURCH,
    POTTERY,
    HISTORY
}

data class PuntoInteres(
    val id: String,
    val titulo: String, 
    val descripcion: String, 
    val latitude: Double,
    val longitude: Double,
    val category: PoiCategory,
    val imageUrl: String = "",
    val narrador: String = "Guía Talavera",
    val radioMetros: Double = 100.0
)
