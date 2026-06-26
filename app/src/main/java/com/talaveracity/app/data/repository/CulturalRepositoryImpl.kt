package com.talaveracity.app.data.repository

import com.talaveracity.app.data.local.*
import com.talaveracity.app.domain.model.CeramicPiece
import com.talaveracity.app.domain.model.CulturalEvent
import com.talaveracity.app.domain.repository.CulturalRepository
import com.talaveracity.app.data.remote.CulturalApiService
import com.talaveracity.app.data.remote.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import com.talaveracity.app.data.remote.TalaveraRssParser
import com.google.mlkit.genai.prompt.Generation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.mlkit.genai.prompt.GenerateContentRequest
import com.google.mlkit.genai.prompt.TextPart
import javax.inject.Inject

class CulturalRepositoryImpl @Inject constructor(
    private val apiService: CulturalApiService,
    private val eventDao: EventDao
) : CulturalRepository {
    private val rssParser = TalaveraRssParser()

    override suspend fun getEvents(): Result<List<CulturalEvent>> {
        return syncOfficialInfo(
            url = "https://www.talavera.es/agenda/feed/",
            type = InfoType.AGENDA,
            fallbackMock = true
        )
    }

    override suspend fun getOfficialNews(): Result<List<CulturalEvent>> {
        return syncOfficialInfo(
            url = "https://www.talavera.es/noticias/feed/",
            type = InfoType.NEWS
        )
    }

    override suspend fun getMunicipalAnnouncements(): Result<List<CulturalEvent>> {
        // En una app real, el ayuntamiento podría tener un feed específico de bandos
        // Por ahora usamos el feed de noticias filtrado o uno de avisos si existiera
        return syncOfficialInfo(
            url = "https://www.talavera.es/noticias/feed/", 
            type = InfoType.ANNOUNCEMENT
        )
    }

    private suspend fun syncOfficialInfo(
        url: String, 
        type: InfoType, 
        fallbackMock: Boolean = false
    ): Result<List<CulturalEvent>> = withContext(Dispatchers.IO) {
        try {
            val responseBody = apiService.getRssFeed(url)
            val events = rssParser.parse(responseBody.byteStream())
            
            if (events.isNotEmpty()) {
                eventDao.deleteOfficialInfoByType(type)
                eventDao.insertOfficialInfo(events.map { it.toOfficialEntity(type) })
                Result.success(events)
            } else {
                fetchCachedOrMock(type, fallbackMock)
            }
        } catch (e: Exception) {
            fetchCachedOrMock(type, fallbackMock)
        }
    }

    private suspend fun fetchCachedOrMock(type: InfoType, fallbackMock: Boolean): Result<List<CulturalEvent>> {
        val cached = eventDao.getOfficialInfoByType(type)
        return if (cached.isNotEmpty()) {
            Result.success(cached.map { it.toDomain() })
        } else if (fallbackMock && type == InfoType.AGENDA) {
            Result.success(getMockEvents())
        } else {
            Result.success(emptyList())
        }
    }

    override fun getSavedEvents(): Flow<List<CulturalEvent>> {
        return eventDao.getAllSavedEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun toggleSaveEvent(event: CulturalEvent) {
        if (eventDao.isEventSaved(event.id)) {
            eventDao.deleteEvent(event.toEntity())
        } else {
            eventDao.saveEvent(event.toEntity())
        }
    }

    override suspend fun isEventSaved(id: String): Boolean {
        return eventDao.isEventSaved(id)
    }

    override suspend fun getEventById(id: String): CulturalEvent? {
        return withContext(Dispatchers.IO) {
            val saved = eventDao.getAllSavedEventsOnce()
            val allCached = InfoType.values().flatMap { eventDao.getOfficialInfoByType(it) }
            
            saved.find { it.id == id }?.toDomain() 
                ?: allCached.find { it.id == id }?.toDomain()
                ?: getMockEvents().find { it.id == id }
        }
    }

    override fun getCollectedPieces(): Flow<List<CeramicPiece>> {
        return eventDao.getAllCollectedPieces().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun collectPiece(piece: CeramicPiece) {
        eventDao.collectPiece(piece.toEntity())
    }

    override suspend fun isPieceCollected(id: String): Boolean {
        return eventDao.isPieceCollected(id)
    }

    override suspend fun generarHistoriaConIA(pieza: CeramicPiece): String? {
        return withContext(Dispatchers.IO) {
            try {
                val model = Generation.getClient()
                val prompt = """
                    Actúa como un experto historiador de cerámica de Talavera de la Reina.
                    Genera una breve historia fascinante (máximo 3 párrafos) sobre la siguiente pieza:
                    Nombre: ${pieza.name}
                    Serie: ${pieza.series}
                    Época: ${pieza.century}
                    
                    Enfócate en la técnica artesanal, el simbolismo de los colores y su importancia cultural.
                    Usa un tono apasionado y educativo.
                """.trimIndent()

                val request = GenerateContentRequest.Builder(TextPart(prompt)).build()
                val response = model.generateContent(request)
                response.text
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun identifyCeramic(label: String): CeramicPiece? {
        return when (label.lowercase()) {
            "pottery", "tableware" -> CeramicPiece(
                id = "serie_azul_01",
                name = "Plato de la Serie Azul",
                series = "Serie Azul (Renacimiento)",
                century = "Siglo XVI",
                description = "La serie más emblemática, caracterizada por su azul cobalto sobre fondo blanco vidriado.",
                imageUrl = "https://images.unsplash.com/photo-1590650516494-0c8e4a4dd67e?auto=format&fit=crop&q=80&w=800"
            )
            "vase", "jug" -> CeramicPiece(
                id = "serie_policroma_01",
                name = "Jarra de la Serie Policroma",
                series = "Serie Policroma Mulatilla",
                century = "Siglo XVIII",
                description = "Destaca por el uso de colores amarillos, anaranjados y verdes cobre.",
                imageUrl = "https://images.unsplash.com/photo-1578321272176-b7bac0429b5a?auto=format&fit=crop&q=80&w=800"
            )
            else -> null
        }
    }

    private fun getMockEvents(): List<CulturalEvent> {
        return listOf(
            CulturalEvent(
                "1",
                "Exposición: Cerámica en el Siglo XXI",
                "15 May - 20 Jun",
                "Museo Ruiz de Luna",
                "https://images.unsplash.com/photo-1578321272176-b7bac0429b5a?auto=format&fit=crop&q=80&w=800",
                "Una mirada contemporánea a las técnicas tradicionales de Talavera."
            ),
            CulturalEvent(
                "2",
                "Feria de San Isidro 2026",
                "14 - 18 Mayo",
                "Recinto Ferial",
                "https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3?auto=format&fit=crop&q=80&w=800",
                "La festividad más grande de la ciudad con conciertos y tradiciones."
            )
        )
    }
}
