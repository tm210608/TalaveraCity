package com.talaveracity.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CeramicPiece(
    val id: String,
    val name: String,
    val series: String,
    val century: String,
    val description: String,
    val imageUrl: String,
    val audioUrl: String? = null,
    val historiaExtendida: String? = null
)




