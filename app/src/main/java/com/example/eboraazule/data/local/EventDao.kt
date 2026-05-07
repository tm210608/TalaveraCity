package com.example.eboraazule.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM saved_events ORDER BY savedAt DESC")
    fun getAllSavedEvents(): Flow<List<SavedEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEvent(event: SavedEventEntity)

    @Delete
    suspend fun deleteEvent(event: SavedEventEntity)

    @Query("SELECT EXISTS(SELECT * FROM saved_events WHERE id = :id)")
    suspend fun isEventSaved(id: String): Boolean
}
