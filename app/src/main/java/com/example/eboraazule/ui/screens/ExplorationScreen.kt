package com.example.eboraazule.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eboraazule.R
import com.example.eboraazule.ui.viewmodel.ExplorationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorationScreen(
    viewModel: ExplorationViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedHotspot = uiState.selectedHotspot

    Box(modifier = Modifier.fillMaxSize()) {
        // City Skyline Background
        AsyncImage(
            model = "https://images.unsplash.com/photo-1543783230-050414a6003b?auto=format&fit=crop&q=80&w=2000",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark overlay for focus
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        // Hotspots
        HotspotMarker(
            modifier = Modifier.align(Alignment.Center).offset(x = (-80).dp, y = (-40).dp),
            onClick = { viewModel.onHotspotSelected(Hotspot.PuenteViejo) }
        )

        HotspotMarker(
            modifier = Modifier.align(Alignment.Center).offset(x = 100.dp, y = 20.dp),
            onClick = { viewModel.onHotspotSelected(Hotspot.CeramicaAzul) }
        )

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                "Sinergia Talaverana",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        AnimatedVisibility(
            visible = selectedHotspot != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            selectedHotspot?.let { hotspot ->
                InfoPanel(hotspot = hotspot, onClose = { viewModel.onHotspotSelected(null) })
            }
        }
    }
}

@Composable
fun HotspotMarker(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
        shape = CircleShape,
        tonalElevation = 8.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Info, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun InfoPanel(hotspot: Hotspot, onClose: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = hotspot.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = hotspot.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onClose,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar")
            }
        }
    }
}

enum class Hotspot(val title: String, val description: String) {
    PuenteViejo(
        "Puente Viejo",
        "Este puente romano-medieval sobre el Tajo es el testigo más antiguo de la ciudad, uniendo las dos orillas y el camino hacia la cerámica."
    ),
    CeramicaAzul(
        "Azul Azur Tradicional",
        "El característico azul de Talavera se obtiene con cobalto, una técnica perfeccionada durante siglos que hoy define nuestro horizonte visual."
    )
}
