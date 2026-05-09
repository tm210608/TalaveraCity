package com.example.eboraazule.data.repository

import com.example.eboraazule.data.local.AzulejoDao
import com.example.eboraazule.data.local.EventDao
import com.example.eboraazule.data.local.toDomain
import com.example.eboraazule.data.local.toEntity
import com.example.eboraazule.data.model.Azulejo
import com.example.eboraazule.data.model.CulturalEvent
import com.example.eboraazule.data.remote.CulturalApiService
import com.example.eboraazule.data.remote.toDomain
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CulturalRepository(
    private val apiService: CulturalApiService,
    private val eventDao: EventDao,
    private val azulejoDao: AzulejoDao
) {
    suspend fun getEvents(): Result<List<CulturalEvent>> {
        return try {
            val response = apiService.getEvents()
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            // Fallback to mock data if API fails, or return failure
            // Result.failure(e)
            Result.success(getMockEvents())
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

    fun getAzulejosColeccionados(): Flow<List<Azulejo>> {
        return azulejoDao.getAllAzulejos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun saveAzulejo(azulejo: Azulejo) {
        azulejoDao.insertAzulejo(azulejo.toEntity())
    }

    suspend fun isAzulejoColeccionado(id: String): Boolean {
        return azulejoDao.isAzulejoColeccionado(id)
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
