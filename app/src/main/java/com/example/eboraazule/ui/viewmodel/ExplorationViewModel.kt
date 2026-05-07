package com.example.eboraazule.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.eboraazule.ui.screens.Hotspot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ExplorationUiState(
    val selectedHotspot: Hotspot? = null
)

class ExplorationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExplorationUiState())
    val uiState: StateFlow<ExplorationUiState> = _uiState.asStateFlow()

    fun onHotspotSelected(hotspot: Hotspot?) {
        _uiState.value = _uiState.value.copy(selectedHotspot = hotspot)
    }
}
