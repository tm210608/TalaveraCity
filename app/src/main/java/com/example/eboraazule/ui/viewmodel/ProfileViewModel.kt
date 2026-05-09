package com.example.eboraazule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eboraazule.data.model.Azulejo
import com.example.eboraazule.data.model.CulturalEvent
import com.example.eboraazule.data.repository.CulturalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: CulturalRepository) : ViewModel() {

    val savedEvents: StateFlow<List<CulturalEvent>> = repository.getSavedEvents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val coleccionAzulejos: StateFlow<List<Azulejo>> = repository.getAzulejosColeccionados()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteEvent(event: CulturalEvent) {
        viewModelScope.launch {
            repository.toggleSaveEvent(event)
        }
    }
}
