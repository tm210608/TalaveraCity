package com.example.eboraazule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eboraazule.data.model.Azulejo
import com.example.eboraazule.data.repository.CulturalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

sealed class EscaneoUiState {
    object Cargando : EscaneoUiState()
    object Listo : EscaneoUiState()
    data class Detectado(val etiqueta: String, val confianza: Float, val guardado: Boolean = false) : EscaneoUiState()
    data class Error(val mensaje: String) : EscaneoUiState()
}

class EscaneoViewModel(private val repository: CulturalRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<EscaneoUiState>(EscaneoUiState.Listo)
    val uiState: StateFlow<EscaneoUiState> = _uiState.asStateFlow()

    fun procesarDeteccion(etiqueta: String, confianza: Float) {
        if (confianza > 0.7f) {
            viewModelScope.launch {
                // Verificar si ya está coleccionado (usando la etiqueta como ID para simplificar este prototipo)
                val yaGuardado = repository.isAzulejoColeccionado(etiqueta)
                _uiState.value = EscaneoUiState.Detectado(etiqueta, confianza, yaGuardado)
            }
        }
    }

    fun guardarAzulejo(etiqueta: String) {
        viewModelScope.launch {
            val azulejo = Azulejo(
                id = etiqueta, // Usamos la etiqueta como ID único en este caso
                nombre = etiqueta,
                descripcion = "Pieza de cerámica identificada como $etiqueta mediante el escáner.",
                imagenUrl = "", // Por ahora sin imagen real de la captura
                tipo = "Talavera Tradicional"
            )
            repository.saveAzulejo(azulejo)
            _uiState.value = EscaneoUiState.Detectado(etiqueta, 1.0f, true)
        }
    }

    fun reiniciarEscaneo() {
        _uiState.value = EscaneoUiState.Listo
    }
}
