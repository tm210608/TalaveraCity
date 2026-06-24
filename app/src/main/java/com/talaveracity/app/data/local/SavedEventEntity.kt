package com.talaveracity.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talaveracity.app.domain.model.CulturalEvent

@Entity(tableName = "saved_events")
data class SavedEventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val date: String,
    val location: String,
    val imageUrl: String,
    val description: String,
    val savedAt: Long = System.currentTimeMillis()
)

fun SavedEventEntity.toDomain(): CulturalEvent {
    return CulturalEvent(
        id = id,
        title = title,
        date = date,
        location = location,
        imageUrl = imageUrl,
        description = description
    )
}

fun CulturalEvent.toEntity(): SavedEventEntity {
    return SavedEventEntity(
        id = id,
        title = title,
        date = date,
        location = location,
        imageUrl = imageUrl,
        description = description
    )
}




