package com.talaveracity.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.talaveracity.app.R
import com.talaveracity.app.domain.model.PoiCategory
import com.talaveracity.app.domain.model.PuntoInteres
import com.talaveracity.app.ui.viewmodel.ExplorationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExplorationScreen(
    viewModel: ExplorationViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val puntoSeleccionado = uiState.puntoInteresSeleccionado
    
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    // Talavera de la Reina coords
    val talavera = LatLng(39.9583, -4.8322)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(talavera, 16f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (locationPermissionState.status.isGranted) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style),
                    isMyLocationEnabled = true
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = true
                ),
                onMapClick = { viewModel.seleccionarPuntoInteres(null) }
            ) {
                uiState.puntos.forEach { punto ->
                    val markerState = rememberMarkerState(position = LatLng(punto.latitude, punto.longitude))
                    
                    Marker(
                        state = markerState,
                        title = punto.titulo,
                        onClick = {
                            viewModel.seleccionarPuntoInteres(punto)
                            false
                        }
                    )
                }
            }

            if (uiState.cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            LocationPermissionRequest(onGrant = { locationPermissionState.launchPermissionRequest() })
        }

        // Header Superior (Glassmorphism)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            color = Color.Black.copy(alpha = 0.4f),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Explora Talavera",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Filtros Rápidos
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 110.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PoiFilterChip(label = "Monumentos", icon = Icons.Rounded.AccountBalance)
            Spacer(modifier = Modifier.width(8.dp))
            PoiFilterChip(label = "Museos", icon = Icons.Rounded.Museum)
            Spacer(modifier = Modifier.width(8.dp))
            PoiFilterChip(label = "Cerámica", icon = Icons.Rounded.Brush)
        }

        // Panel de información inferior
        AnimatedVisibility(
            visible = puntoSeleccionado != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            puntoSeleccionado?.let { punto ->
                InfoPanel(
                    punto = punto, 
                    onClose = { viewModel.seleccionarPuntoInteres(null) },
                    estaReproduciendo = uiState.estaReproduciendoAudio,
                    progreso = uiState.progresoAudio,
                    onToggleAudio = { viewModel.conmutarAudio() }
                )
            }
        }
    }
}

@Composable
fun PoiFilterChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        modifier = Modifier.height(40.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun InfoPanel(
    punto: PuntoInteres, 
    onClose: () -> Unit,
    estaReproduciendo: Boolean,
    progreso: Float,
    onToggleAudio: () -> Unit
) {
    val context = LocalContext.current
    
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            if (punto.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = punto.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = punto.titulo,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = when(punto.category) {
                                PoiCategory.MONUMENT -> "Monumento Histórico"
                                PoiCategory.MUSEUM -> "Museo y Colección"
                                PoiCategory.CHURCH -> "Arquitectura Religiosa"
                                PoiCategory.POTTERY -> "Taller Cerámico"
                                PoiCategory.HISTORY -> "Lugar Histórico"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    IconButton(onClick = onClose) {
                        Icon(Icons.Rounded.Close, contentDescription = "Cerrar")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                AudioCapsulaCard(
                    narrador = punto.narrador,
                    estaReproduciendo = estaReproduciendo,
                    progreso = progreso,
                    onToggle = onToggleAudio
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = punto.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 24.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { 
                        val gmmIntentUri = Uri.parse("google.navigation:q=${punto.latitude},${punto.longitude}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        context.startActivity(mapIntent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Icon(Icons.Rounded.Directions, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cómo llegar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AudioCapsulaCard(
    narrador: String,
    estaReproduciendo: Boolean,
    progreso: Float,
    onToggle: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggle,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (estaReproduciendo) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    "Audioguía: Susurros del Tajo",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Narrado por: $narrador",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            }
        }
    }
}

@Composable
fun LocationPermissionRequest(onGrant: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Rounded.LocationOn, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            stringResource(R.string.access_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.access_desc),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onGrant) {
            Text(stringResource(R.string.btn_allow_location))
        }
    }
}
