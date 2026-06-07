package com.example.eboraazule.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.eboraazule.data.model.CeramicPiece

@Entity(tableName = "collected_pieces")
data class CeramicPieceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val series: String,
    val century: String,
    val description: String,
    val imageUrl: String,
    val collectedAt: Long = System.currentTimeMillis()
)

fun CeramicPieceEntity.toDomain() = CeramicPiece(
    id = id,
    name = name,
    series = series,
    century = century,
    description = description,
    imageUrl = imageUrl
)

fun CeramicPiece.toEntity() = CeramicPieceEntity(
    id = id,
    name = name,
    series = series,
    century = century,
    description = description,
    imageUrl = imageUrl
)
