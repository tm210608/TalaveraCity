package com.talaveracity.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talaveracity.app.domain.model.CulturalEvent
import com.talaveracity.app.domain.repository.CulturalRepository
import com.talaveracity.app.util.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EventDetailUiState {
    data object Cargando : EventDetailUiState()
    data class Exito(val evento: CulturalEvent, val isSaved: Boolean) : EventDetailUiState()
    data class Error(val mensaje: String) : EventDetailUiState()
}

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: CulturalRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow<EventDetailUiState>(EventDetailUiState.Cargando)
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            val event = repository.getEventById(eventId)
            if (event != null) {
                val isSaved = repository.isEventSaved(eventId)
                _uiState.value = EventDetailUiState.Exito(event, isSaved)
            } else {
                _uiState.value = EventDetailUiState.Error("Evento no encontrado")
            }
        }
    }

    fun programarRecordatorio(event: CulturalEvent) {
        // En una app real calcularíamos el tiempo real del evento
        // Para la demo, programamos a los 10 segundos
        notificationHelper.scheduleNotification(
            title = "TalaveraCity: ${event.title}",
            message = "¡Tu evento en ${event.location} comienza pronto!",
            delayMillis = 10000 
        )
    }

    fun conmutarGuardado(event: CulturalEvent) {
        viewModelScope.launch {
            repository.toggleSaveEvent(event)
            val isSaved = repository.isEventSaved(event.id)
            val current = _uiState.value
            if (current is EventDetailUiState.Exito) {
                _uiState.value = current.copy(isSaved = isSaved)
            }
        }
    }
}




