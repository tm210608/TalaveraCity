package com.talaveracity.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talaveracity.app.domain.model.CeramicPiece
import com.talaveracity.app.domain.repository.CulturalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CollectionUiState(
    val pieces: List<CeramicPiece> = emptyList()
)

@HiltViewModel
class CollectionViewModel @Inject constructor(private val repository: CulturalRepository) : ViewModel() {

    val uiState: StateFlow<CollectionUiState> = repository.getCollectedPieces()
        .map { CollectionUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CollectionUiState()
        )
}




