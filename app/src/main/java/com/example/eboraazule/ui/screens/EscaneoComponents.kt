package com.example.eboraazule.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.eboraazule.ui.viewmodel.EscaneoUiState

@Composable
fun CenefaTalaveraOverlay() {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val margin = 40.dp.toPx()
        val cornerLength = 60.dp.toPx()
        val strokeWidth = 4.dp.toPx()

        // Esquinas decorativas inspiradas en Talavera (Azul y Amarillo)
        // Arriba Izquierda
        drawLine(primaryColor, Offset(margin, margin), Offset(margin + cornerLength, margin), strokeWidth)
        drawLine(primaryColor, Offset(margin, margin), Offset(margin, margin + cornerLength), strokeWidth)
        
        // Arriba Derecha
        drawLine(secondaryColor, Offset(width - margin, margin), Offset(width - margin - cornerLength, margin), strokeWidth)
        drawLine(secondaryColor, Offset(width - margin, margin), Offset(width - margin, margin + cornerLength), strokeWidth)
        
        // Abajo Izquierda
        drawLine(secondaryColor, Offset(margin, height - margin), Offset(margin + cornerLength, height - margin), strokeWidth)
        drawLine(secondaryColor, Offset(margin, height - margin), Offset(margin, height - margin - cornerLength), strokeWidth)
        
        // Abajo Derecha
        drawLine(primaryColor, Offset(width - margin, height - margin), Offset(width - margin - cornerLength, height - margin), strokeWidth)
        drawLine(primaryColor, Offset(width - margin, height - margin), Offset(width - margin, height - margin - cornerLength), strokeWidth)
    }
}

@Composable
fun ScanningFeedback(
    uiState: EscaneoUiState, 
    onReset: () -> Unit,
    onSave: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        when (uiState) {
            is EscaneoUiState.Detectado -> {
                Card(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "¡Obra Identificada!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            uiState.etiqueta,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = onReset, shape = RoundedCornerShape(12.dp)) {
                                Text("Reintentar")
                            }
                            
                            if (uiState.guardado) {
                                Button(
                                    onClick = {}, 
                                    enabled = false,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        disabledContainerColor = Color(0xFF4CAF50),
                                        disabledContentColor = Color.White
                                    )
                                ) {
                                    Text("¡Guardado!")
                                }
                            } else {
                                Button(
                                    onClick = { onSave(uiState.etiqueta) },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Guardar Colección")
                                }
                            }
                        }
                    }
                }
            }
            is EscaneoUiState.Listo -> {
                Surface(
                    modifier = Modifier.padding(bottom = 48.dp),
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Info, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Encuadra una pieza de cerámica", color = Color.White)
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun PermissionDeniedContent(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Rounded.Camera, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Text("Se requiere acceso a la cámara", color = Color.White, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRequestPermission) {
            Text("Conceder Permiso")
        }
    }
}
