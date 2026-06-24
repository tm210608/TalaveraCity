package com.talaveracity.app.data.remote

import com.talaveracity.app.domain.model.CulturalEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CulturalEventDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("date") val date: String,
    @SerialName("location") val location: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("description") val description: String
)

fun CulturalEventDto.toDomain(): CulturalEvent {
    return CulturalEvent(
        id = id,
        title = title,
        date = date,
        location = location,
        imageUrl = imageUrl,
        description = description
    )
}




