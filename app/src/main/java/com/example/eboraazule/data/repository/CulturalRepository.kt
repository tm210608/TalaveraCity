package com.example.eboraazule.data.repository

import com.example.eboraazule.data.local.*
import com.example.eboraazule.data.model.CeramicPiece
import com.example.eboraazule.data.model.CulturalEvent
import com.example.eboraazule.data.remote.CulturalApiService
import com.example.eboraazule.data.remote.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import com.example.eboraazule.data.remote.TalaveraRssParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CulturalRepository(
    private val apiService: CulturalApiService,
    private val eventDao: EventDao
) {
    private val rssParser = TalaveraRssParser()

    suspend fun getEvents(): Result<List<CulturalEvent>> {
        return withContext(Dispatchers.IO) {
            try {
                val responseBody = apiService.getRssFeed("https://www.talavera.es/agenda/feed/")
                val events = rssParser.parse(responseBody.byteStream())
                
                if (events.isNotEmpty()) {
                    eventDao.deleteCachedEvents(isNews = false)
                    eventDao.insertCachedEvents(events.map { it.toCachedEntity(isNews = false) })
                    Result.success(events)
                } else {
                    val cached = eventDao.getCachedEvents()
                    if (cached.isNotEmpty()) {
                        Result.success(cached.map { it.toDomain() })
                    } else {
                        Result.success(getMockEvents())
                    }
                }
            } catch (e: Exception) {
                val cached = eventDao.getCachedEvents()
                if (cached.isNotEmpty()) {
                    Result.success(cached.map { it.toDomain() })
                } else {
                    Result.success(getMockEvents())
                }
            }
        }
    }

    suspend fun getOfficialNews(): Result<List<CulturalEvent>> {
        return withContext(Dispatchers.IO) {
            try {
                val responseBody = apiService.getRssFeed("https://www.talavera.es/noticias/feed/")
                val news = rssParser.parse(responseBody.byteStream())
                
                if (news.isNotEmpty()) {
                    eventDao.deleteCachedEvents(isNews = true)
                    eventDao.insertCachedEvents(news.map { it.toCachedEntity(isNews = true) })
                    Result.success(news)
                } else {
                    val cached = eventDao.getCachedNews()
                    Result.success(cached.map { it.toDomain() })
                }
            } catch (e: Exception) {
                val cached = eventDao.getCachedNews()
                if (cached.isNotEmpty()) {
                    Result.success(cached.map { it.toDomain() })
                } else {
                    Result.failure(e)
                }
            }
        }
    }

    fun getSavedEvents(): Flow<List<CulturalEvent>> {
        return eventDao.getAllSavedEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun toggleSaveEvent(event: CulturalEvent) {
        if (eventDao.isEventSaved(event.id)) {
            eventDao.deleteEvent(event.toEntity())
        } else {
            eventDao.saveEvent(event.toEntity())
        }
    }

    suspend fun isEventSaved(id: String): Boolean {
        return eventDao.isEventSaved(id)
    }

    suspend fun getEventById(id: String): CulturalEvent? {
        return withContext(Dispatchers.IO) {
            // Buscar en guardados, caché o mocks
            val saved = eventDao.getAllSavedEventsOnce()
            saved.find { it.id == id }?.toDomain() 
                ?: eventDao.getCachedEvents().find { it.id == id }?.toDomain()
                ?: eventDao.getCachedNews().find { it.id == id }?.toDomain()
                ?: getMockEvents().find { it.id == id }
        }
    }

    // Lógica de Colección de Cerámica
    fun getCollectedPieces(): Flow<List<CeramicPiece>> {
        return eventDao.getAllCollectedPieces().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun collectPiece(piece: CeramicPiece) {
        eventDao.collectPiece(piece.toEntity())
    }

    suspend fun isPieceCollected(id: String): Boolean {
        return eventDao.isPieceCollected(id)
    }

    fun identifyCeramic(label: String): CeramicPiece? {
        // En una app real, esto podría llamar a una API o usar un modelo local más complejo
        // Mapeamos etiquetas comunes de ML Kit a series reales de Talavera
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
            ),
            CulturalEvent(
                "3",
                "Taller de Alfarería en Vivo",
                "Todos los Sábados",
                "Plaza del Pan",
                "https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?auto=format&fit=crop&q=80&w=800",
                "Aprende de los maestros artesanos locales en un entorno histórico."
            ),
            CulturalEvent(
                "4",
                "Ruta Nocturna: Murallas y Torres",
                "Viernes Noche",
                "Punto de Encuentro: Oficina de Turismo",
                "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&q=80&w=800",
                "Descubre los secretos de la ciudad bajo la luz de la luna."
            )
        )
    }
}
