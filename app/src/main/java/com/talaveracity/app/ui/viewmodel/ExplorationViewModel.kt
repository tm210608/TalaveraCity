package com.talaveracity.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talaveracity.app.domain.model.PuntoInteres
import com.talaveracity.app.domain.repository.CulturalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExploracionUiState(
    val puntos: List<PuntoInteres> = emptyList(),
    val puntoInteresSeleccionado: PuntoInteres? = null,
    val estaReproduciendoAudio: Boolean = false,
    val progresoAudio: Float = 0f,
    val cargando: Boolean = true
)

@HiltViewModel
class ExplorationViewModel @Inject constructor(
    private val repository: CulturalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploracionUiState())
    val uiState: StateFlow<ExploracionUiState> = _uiState.asStateFlow()

    init {
        loadPuntos()
    }

    private fun loadPuntos() {
        viewModelScope.launch {
            repository.syncPoisIfNeeded()
            repository.getAllPois().collect { lista ->
                _uiState.update { it.copy(puntos = lista, cargando = false) }
            }
        }
    }

    fun seleccionarPuntoInteres(punto: PuntoInteres?) {
        _uiState.update { 
            it.copy(
                puntoInteresSeleccionado = punto,
                estaReproduciendoAudio = false,
                progresoAudio = 0f
            )
        }
    }

    fun conmutarAudio() {
        val actual = _uiState.value.estaReproduciendoAudio
        _uiState.update { it.copy(estaReproduciendoAudio = !actual) }
        
        if (!actual) {
            simularProgresoAudio()
        }
    }

    private fun simularProgresoAudio() {
        viewModelScope.launch {
            while (_uiState.value.estaReproduciendoAudio && _uiState.value.progresoAudio < 1f) {
                delay(100)
                _uiState.update { 
                    val nuevoProgreso = it.progresoAudio + 0.01f
                    if (nuevoProgreso >= 1f) {
                        it.copy(estaReproduciendoAudio = false, progresoAudio = 1f)
                    } else {
                        it.copy(progresoAudio = nuevoProgreso)
                    }
                }
            }
        }
    }
}
