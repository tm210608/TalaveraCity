package com.talaveracity.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talaveracity.app.domain.model.CulturalEvent
import com.talaveracity.app.domain.model.PuntoInteres
import com.talaveracity.app.domain.repository.CulturalRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EventosUiState {
    data object Cargando : EventosUiState()
    data class Exito(
        val eventos: List<CulturalEvent>,
        val noticiasOficiales: List<CulturalEvent> = emptyList(),
        val idsEventosGuardados: Set<String> = emptySet(),
        val puntoCercano: PuntoInteres? = null
    ) : EventosUiState()
    data class Error(val mensaje: String) : EventosUiState()
}

@HiltViewModel
class EventsViewModel @Inject constructor(private val repository: CulturalRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<EventosUiState>(EventosUiState.Cargando)
    val uiState: StateFlow<EventosUiState> = _uiState.asStateFlow()

    init {
        recargarTodo()
        observarEventosGuardados()
    }

    fun recargarTodo() {
        viewModelScope.launch {
            _uiState.value = EventosUiState.Cargando
            
            val resultEventos = repository.getEvents()
            val resultNoticias = repository.getOfficialNews()

            if (resultEventos.isSuccess) {
                val eventos = resultEventos.getOrNull() ?: emptyList()
                val noticias = resultNoticias.getOrNull() ?: emptyList()
                
                _uiState.value = EventosUiState.Exito(
                    eventos = eventos,
                    noticiasOficiales = noticias
                )
                
                // Cargar IDs de guardados
                repository.getSavedEvents().take(1).collect { eventosGuardados ->
                    val actual = _uiState.value
                    if (actual is EventosUiState.Exito) {
                        _uiState.value = actual.copy(idsEventosGuardados = eventosGuardados.map { it.id }.toSet())
                    }
                }
            } else {
                _uiState.value = EventosUiState.Error(resultEventos.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    fun conmutarGuardadoEvento(evento: CulturalEvent) {
        viewModelScope.launch {
            repository.toggleSaveEvent(evento)
        }
    }

    fun actualizarUbicacion(latLng: LatLng) {
        val actual = _uiState.value
        if (actual is EventosUiState.Exito) {
            val puntoMasCercano = PuntoInteres.values().find { punto ->
                calcularDistancia(latLng, punto.coords) < punto.radioMetros
            }
            if (actual.puntoCercano != puntoMasCercano) {
                _uiState.value = actual.copy(puntoCercano = puntoMasCercano)
            }
        }
    }

    private fun calcularDistancia(start: LatLng, end: LatLng): Double {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            start.latitude, start.longitude,
            end.latitude, end.longitude,
            results
        )
        return results[0].toDouble()
    }

    private fun observarEventosGuardados() {
        viewModelScope.launch {
            repository.getSavedEvents().collect { eventosGuardados ->
                val actual = _uiState.value
                if (actual is EventosUiState.Exito) {
                    _uiState.value = actual.copy(
                        idsEventosGuardados = eventosGuardados.map { it.id }.toSet()
                    )
                }
            }
        }
    }
}




