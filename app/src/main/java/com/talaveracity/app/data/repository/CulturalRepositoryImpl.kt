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

    override suspend fun getOfficialNews(): Result<List<CulturalEvent>> {
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
            saved.find { it.id == id }?.toDomain() 
                ?: eventDao.getCachedEvents().find { it.id == id }?.toDomain()
                ?: eventDao.getCachedNews().find { it.id == id }?.toDomain()
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




