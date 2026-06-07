package com.example.eboraazule.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.eboraazule.data.model.CulturalEvent

@Entity(tableName = "cached_events")
data class CachedEventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val date: String,
    val location: String,
    val imageUrl: String,
    val description: String,
    val isNews: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)

fun CachedEventEntity.toDomain(): CulturalEvent {
    return CulturalEvent(
        id = id,
        title = title,
        date = date,
        location = location,
        imageUrl = imageUrl,
        description = description
    )
}

fun CulturalEvent.toCachedEntity(isNews: Boolean): CachedEventEntity {
    return CachedEventEntity(
        id = id,
        title = title,
        date = date,
        location = location,
        imageUrl = imageUrl,
        description = description,
        isNews = isNews
    )
}
