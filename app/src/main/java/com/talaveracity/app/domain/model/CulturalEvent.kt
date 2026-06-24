package com.talaveracity.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CulturalEvent(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val imageUrl: String,
    val description: String
)




