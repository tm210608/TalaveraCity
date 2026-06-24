package com.talaveracity.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talaveracity.app.domain.model.CeramicPiece
import com.talaveracity.app.domain.repository.CulturalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EscaneoUiState {
    data object Listo : EscaneoUiState()
    data class Identificado(
        val pieza: CeramicPiece, 
        val yaColeccionada: Boolean,
        val historiaIA: String? = null,
        val generandoHistoria: Boolean = false
    ) : EscaneoUiState()
    data class Error(val mensaje: String) : EscaneoUiState()
}

@HiltViewModel
class EscaneoViewModel @Inject constructor(private val repository: CulturalRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<EscaneoUiState>(EscaneoUiState.Listo)
    val uiState: StateFlow<EscaneoUiState> = _uiState.asStateFlow()

    fun procesarDeteccion(etiqueta: String, confianza: Float) {
        if (_uiState.value !is EscaneoUiState.Listo) return 

        viewModelScope.launch {
            val pieza = repository.identifyCeramic(etiqueta)
            if (pieza != null) {
                val yaColeccionada = repository.isPieceCollected(pieza.id)
                _uiState.value = EscaneoUiState.Identificado(pieza, yaColeccionada)
                
                // Iniciar generación de historia con IA automáticamente
                generarHistoriaConIA(pieza)
            }
        }
    }

    private fun generarHistoriaConIA(pieza: CeramicPiece) {
        val estadoActual = _uiState.value
        if (estadoActual is EscaneoUiState.Identificado) {
            _uiState.value = estadoActual.copy(generandoHistoria = true)
            
            viewModelScope.launch {
                val historia = repository.generarHistoriaConIA(pieza)
                val nuevoEstado = _uiState.value
                if (nuevoEstado is EscaneoUiState.Identificado && nuevoEstado.pieza.id == pieza.id) {
                    _uiState.value = nuevoEstado.copy(
                        historiaIA = historia,
                        generandoHistoria = false
                    )
                }
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




