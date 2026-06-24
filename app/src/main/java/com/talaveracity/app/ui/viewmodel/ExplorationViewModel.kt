package com.talaveracity.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talaveracity.app.domain.model.PuntoInteres
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExploracionUiState(
    val puntoInteresSeleccionado: PuntoInteres? = null,
    val estaReproduciendoAudio: Boolean = false,
    val progresoAudio: Float = 0f
)

@HiltViewModel
class ExplorationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ExploracionUiState())
    val uiState: StateFlow<ExploracionUiState> = _uiState.asStateFlow()

    fun seleccionarPuntoInteres(punto: PuntoInteres?) {
        _uiState.value = _uiState.value.copy(
            puntoInteresSeleccionado = punto,
            estaReproduciendoAudio = false,
            progresoAudio = 0f
        )
    }

    fun conmutarAudio() {
        val actual = _uiState.value.estaReproduciendoAudio
        _uiState.value = _uiState.value.copy(estaReproduciendoAudio = !actual)
        
        // Simulación de progreso si empieza a reproducir
        if (!actual) {
            simularProgresoAudio()
        }
    }

    private fun simularProgresoAudio() {
        viewModelScope.launch {
            while (_uiState.value.estaReproduciendoAudio && _uiState.value.progresoAudio < 1f) {
                delay(100)
                val nuevoProgreso = _uiState.value.progresoAudio + 0.01f
                if (nuevoProgreso >= 1f) {
                    _uiState.value = _uiState.value.copy(estaReproduciendoAudio = false, progresoAudio = 1f)
                } else {
                    _uiState.value = _uiState.value.copy(progresoAudio = nuevoProgreso)
                }
            }
        }
    }
}




