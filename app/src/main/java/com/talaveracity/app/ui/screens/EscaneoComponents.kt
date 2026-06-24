package com.talaveracity.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.talaveracity.app.domain.model.CeramicPiece
import com.talaveracity.app.ui.viewmodel.EscaneoUiState

@Composable
fun CenefaTalaveraOverlay(isIdentified: Boolean = false) {
    val primaryColor = if (isIdentified) Color(0xFF0047AB) else MaterialTheme.colorScheme.primary
    val secondaryColor = if (isIdentified) Color(0xFF0047AB) else MaterialTheme.colorScheme.secondary
    
    val infiniteTransition = rememberInfiniteTransition(label = "scanning")
    val scanOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanLine"
    )

    val strokeWidthAnim by animateFloatAsState(
        targetValue = if (isIdentified) 8f else 4f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
        label = "strokeWidth"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val margin = 40.dp.toPx()
        val cornerLength = 60.dp.toPx()
        val strokeWidth = strokeWidthAnim.dp.toPx()

        // Línea de escaneo animada
        if (!isIdentified) {
            val currentY = margin + (height - 2 * margin) * scanOffset
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, primaryColor.copy(alpha = 0.5f), Color.Transparent)
                ),
                start = Offset(margin, currentY),
                end = Offset(width - margin, currentY),
                strokeWidth = 2.dp.toPx()
            )
        }

        // Esquinas decorativas inspiradas en Talavera
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
    onCollect: (CeramicPiece) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(), 
        contentAlignment = Alignment.BottomCenter
    ) {
        when (uiState) {
            is EscaneoUiState.Identificado -> {
                CeramicIdentificationCard(
                    pieza = uiState.pieza,
                    yaColeccionada = uiState.yaColeccionada,
                    historiaIA = uiState.historiaIA,
                    generandoHistoria = uiState.generandoHistoria,
                    onReset = onReset,
                    onCollect = { onCollect(uiState.pieza) }
                )
            }
            is EscaneoUiState.Listo -> {
                Surface(
                    modifier = Modifier.padding(bottom = 64.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.FilterCenterFocus, 
                            contentDescription = null, 
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "Encuadra una pieza de cerámica", 
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun CeramicIdentificationCard(
    pieza: CeramicPiece,
    yaColeccionada: Boolean,
    historiaIA: String? = null,
    generandoHistoria: Boolean = false,
    onReset: () -> Unit,
    onCollect: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .heightIn(max = 500.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    AsyncImage(
                        model = pieza.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                
                Spacer(Modifier.width(16.dp))
                
                Column {
                    Text(
                        pieza.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        pieza.series,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            Text(
                pieza.description,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Sección de IA Local (Gemini Nano)
            Spacer(Modifier.height(20.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.AutoAwesome, 
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Relato del Alfarero (IA Local)",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(Modifier.height(12.dp))
                    
                    if (generandoHistoria) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth().height(2.dp).clip(CircleShape),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Consultando a los antiguos maestros...",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    } else if (historiaIA != null) {
                        Text(
                            historiaIA,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            "Gemini Nano no está disponible en este dispositivo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reiniciar")
                }
                
                Spacer(Modifier.width(12.dp))
                
                Button(
                    onClick = if (yaColeccionada) ({}) else onCollect,
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (yaColeccionada) Color.Gray else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        if (yaColeccionada) Icons.Rounded.CheckCircle else Icons.Rounded.AutoAwesome, 
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (yaColeccionada) "Coleccionada" else "Coleccionar")
                }
            }
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




