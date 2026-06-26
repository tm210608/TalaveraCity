package com.talaveracity.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talaveracity.app.domain.model.CulturalEvent

enum class InfoType {
    NEWS,      // Noticias generales
    AGENDA,    // Eventos programados
    ANNOUNCEMENT // Bandos municipales / Avisos
}

@Entity(tableName = "official_info")
data class OfficialInfoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val date: String,
    val location: String,
    val imageUrl: String,
    val description: String,
    val type: InfoType,
    val cachedAt: Long = System.currentTimeMillis()
)

fun OfficialInfoEntity.toDomain(): CulturalEvent {
    return CulturalEvent(
        id = id,
        title = title,
        date = date,
        location = location,
        imageUrl = imageUrl,
        description = description
    )
}

fun CulturalEvent.toOfficialEntity(type: InfoType): OfficialInfoEntity {
    return OfficialInfoEntity(
        id = id,
        title = title,
        date = date,
        location = location,
        imageUrl = imageUrl,
        description = description,
        type = type
    )
}
