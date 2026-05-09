package com.example.eboraazule.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.eboraazule.data.model.Azulejo

@Entity(tableName = "azulejos_coleccionados")
data class AzulejoEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val descripcion: String,
    val imagenUrl: String,
    val tipo: String,
    val coleccionadoEn: Long = System.currentTimeMillis()
)

fun AzulejoEntity.toDomain(): Azulejo {
    return Azulejo(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        imagenUrl = imagenUrl,
        tipo = tipo
    )
}

fun Azulejo.toEntity(): AzulejoEntity {
    return AzulejoEntity(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        imagenUrl = imagenUrl,
        tipo = tipo
    )
}
