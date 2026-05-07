package com.example.eboraazule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eboraazule.data.model.CulturalEvent
import com.example.eboraazule.data.repository.CulturalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

sealed class EventsUiState {
    object Loading : EventsUiState()
    data class Success(
        val events: List<CulturalEvent>,
        val savedEventIds: Set<String> = emptySet()
    ) : EventsUiState()
    data class Error(val message: String) : EventsUiState()
}

class EventsViewModel(private val repository: CulturalRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<EventsUiState>(EventsUiState.Loading)
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
        observeSavedEvents()
    }

    private fun observeSavedEvents() {
        viewModelScope.launch {
            repository.getSavedEvents().collect { savedEvents ->
                val current = _uiState.value
                if (current is EventsUiState.Success) {
                    _uiState.value = current.copy(savedEventIds = savedEvents.map { it.id }.toSet())
                }
            }
        }
    }

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = EventsUiState.Loading
            repository.getEvents().fold(
                onSuccess = { events ->
                    _uiState.value = EventsUiState.Success(events)
                    // Re-trigger saved events collection to ensure we have the IDs
                    repository.getSavedEvents().take(1).collect { savedEvents ->
                        val current = _uiState.value
                        if (current is EventsUiState.Success) {
                            _uiState.value = current.copy(savedEventIds = savedEvents.map { it.id }.toSet())
                        }
                    }
                },
                onFailure = { error ->
                    _uiState.value = EventsUiState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }

    fun toggleSaveEvent(event: CulturalEvent) {
        viewModelScope.launch {
            repository.toggleSaveEvent(event)
        }
    }
}
