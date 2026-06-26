package com.talaveracity.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM saved_events ORDER BY savedAt DESC")
    fun getAllSavedEvents(): Flow<List<SavedEventEntity>>

    @Query("SELECT * FROM saved_events ORDER BY savedAt DESC")
    suspend fun getAllSavedEventsOnce(): List<SavedEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEvent(event: SavedEventEntity)

    @Delete
    suspend fun deleteEvent(event: SavedEventEntity)

    @Query("SELECT EXISTS(SELECT * FROM saved_events WHERE id = :id)")
    suspend fun isEventSaved(id: String): Boolean

    // Métodos para caché de eventos y noticias
    @Query("SELECT * FROM cached_events WHERE isNews = 0 ORDER BY cachedAt DESC")
    suspend fun getCachedEvents(): List<CachedEventEntity>

    @Query("SELECT * FROM cached_events WHERE isNews = 1 ORDER BY cachedAt DESC")
    suspend fun getCachedNews(): List<CachedEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedEvents(events: List<CachedEventEntity>)

    @Query("DELETE FROM cached_events WHERE isNews = :isNews")
    suspend fun deleteCachedEvents(isNews: Boolean)

    // Métodos para colección de cerámica
    @Query("SELECT * FROM collected_pieces ORDER BY collectedAt DESC")
    fun getAllCollectedPieces(): Flow<List<CeramicPieceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun collectPiece(piece: CeramicPieceEntity)

    @Query("SELECT EXISTS(SELECT * FROM collected_pieces WHERE id = :id)")
    suspend fun isPieceCollected(id: String): Boolean

    // Métodos para Información Oficial (Noticias, Agenda, Bandos)
    @Query("SELECT * FROM official_info WHERE type = :type ORDER BY cachedAt DESC")
    suspend fun getOfficialInfoByType(type: InfoType): List<OfficialInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfficialInfo(info: List<OfficialInfoEntity>)

    @Query("DELETE FROM official_info WHERE type = :type")
    suspend fun deleteOfficialInfoByType(type: InfoType)

    // --- NUEVOS MÉTODOS PARA POIs ---
    @Query("SELECT * FROM points_of_interest")
    fun getAllPois(): Flow<List<PoiEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPois(pois: List<PoiEntity>)

    @Query("SELECT COUNT(*) FROM points_of_interest")
    suspend fun getPoiCount(): Int
}
