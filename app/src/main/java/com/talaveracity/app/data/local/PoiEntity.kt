package com.talaveracity.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talaveracity.app.domain.model.PoiCategory
import com.talaveracity.app.domain.model.PuntoInteres

@Entity(tableName = "points_of_interest")
data class PoiEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val category: PoiCategory,
    val imageUrl: String,
    val narrador: String,
    val radioMetros: Double
)

fun PoiEntity.toDomain(): PuntoInteres {
    return PuntoInteres(
        id = id,
        titulo = title,
        descripcion = description,
        latitude = latitude,
        longitude = longitude,
        category = category,
        imageUrl = imageUrl,
        narrador = narrador,
        radioMetros = radioMetros
    )
}

fun PuntoInteres.toEntity(): PoiEntity {
    return PoiEntity(
        id = id,
        title = titulo,
        description = descripcion,
        latitude = latitude,
        longitude = longitude,
        category = category,
        imageUrl = imageUrl,
        narrador = narrador,
        radioMetros = radioMetros
    )
}
