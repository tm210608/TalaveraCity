package com.example.eboraazule.data.remote

import com.example.eboraazule.data.model.CulturalEvent
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CulturalEventDto(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "date") val date: String,
    @Json(name = "location") val location: String,
    @Json(name = "imageUrl") val imageUrl: String,
    @Json(name = "description") val description: String
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
