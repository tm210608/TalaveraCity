package com.talaveracity.app.domain.repository

import com.talaveracity.app.domain.model.CeramicPiece
import com.talaveracity.app.domain.model.CulturalEvent
import kotlinx.coroutines.flow.Flow

interface CulturalRepository {
    suspend fun getEvents(): Result<List<CulturalEvent>>
    suspend fun getOfficialNews(): Result<List<CulturalEvent>>
    fun getSavedEvents(): Flow<List<CulturalEvent>>
    suspend fun toggleSaveEvent(event: CulturalEvent)
    suspend fun isEventSaved(id: String): Boolean
    suspend fun getEventById(id: String): CulturalEvent?
    
    // Cerámica
    fun getCollectedPieces(): Flow<List<CeramicPiece>>
    suspend fun collectPiece(piece: CeramicPiece)
    suspend fun isPieceCollected(id: String): Boolean
    suspend fun generarHistoriaConIA(pieza: CeramicPiece): String?
    fun identifyCeramic(label: String): CeramicPiece?

    // Puntos de Interés (Mapa)
    fun getAllPois(): Flow<List<PuntoInteres>>
    suspend fun syncPoisIfNeeded()
}




