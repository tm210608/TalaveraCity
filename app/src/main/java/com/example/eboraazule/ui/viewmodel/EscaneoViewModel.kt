package com.example.eboraazule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eboraazule.data.model.CeramicPiece
import com.example.eboraazule.data.repository.CulturalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class EscaneoUiState {
    object Listo : EscaneoUiState()
    data class Identificado(
        val pieza: CeramicPiece, 
        val yaColeccionada: Boolean
    ) : EscaneoUiState()
    data class Error(val mensaje: String) : EscaneoUiState()
}

class EscaneoViewModel(private val repository: CulturalRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<EscaneoUiState>(EscaneoUiState.Listo)
    val uiState: StateFlow<EscaneoUiState> = _uiState.asStateFlow()

    fun procesarDeteccion(etiqueta: String, confianza: Float) {
        if (_uiState.value !is EscaneoUiState.Listo) return // Evitar múltiples detecciones simultáneas

        viewModelScope.launch {
            val pieza = repository.identifyCeramic(etiqueta)
            if (pieza != null) {
                val yaColeccionada = repository.isPieceCollected(pieza.id)
                _uiState.value = EscaneoUiState.Identificado(pieza, yaColeccionada)
            }
        }
    }

    fun coleccionarPieza(pieza: CeramicPiece) {
        viewModelScope.launch {
            repository.collectPiece(pieza)
            _uiState.value = EscaneoUiState.Identificado(pieza, true)
        }
    }

    fun reiniciarEscaneo() {
        _uiState.value = EscaneoUiState.Listo
    }
}
