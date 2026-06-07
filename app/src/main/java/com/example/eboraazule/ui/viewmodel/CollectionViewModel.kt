package com.example.eboraazule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eboraazule.data.model.CeramicPiece
import com.example.eboraazule.data.repository.CulturalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CollectionUiState(
    val pieces: List<CeramicPiece> = emptyList()
)

class CollectionViewModel(private val repository: CulturalRepository) : ViewModel() {

    val uiState: StateFlow<CollectionUiState> = repository.getCollectedPieces()
        .map { CollectionUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CollectionUiState()
        )
}
