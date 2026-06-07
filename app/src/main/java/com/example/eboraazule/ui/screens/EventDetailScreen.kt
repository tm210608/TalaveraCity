package com.example.eboraazule.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.eboraazule.R
import com.example.eboraazule.ui.components.GrecaTalavera
import com.example.eboraazule.ui.viewmodel.EventDetailUiState
import com.example.eboraazule.ui.viewmodel.EventDetailViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.animation.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun EventDetailScreen(
    eventId: String,
    viewModel: EventDetailViewModel,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    FilledTonalIconButton(
                        onClick = onBack,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    if (uiState is EventDetailUiState.Exito) {
                        val estado = uiState as EventDetailUiState.Exito
                        
                        Row(modifier = Modifier.padding(end = 8.dp)) {
                            // Botón de Compartir
                            FilledTonalIconButton(
                                onClick = { 
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "¡Mira este evento en EboraAzule: ${estado.evento.title}!\n\n${estado.evento.description}")
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, null)
                                    context.startActivity(shareIntent)
                                },
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                                )
                            ) {
                                Icon(Icons.Rounded.Share, contentDescription = stringResource(R.string.btn_share))
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            FilledTonalIconButton(
                                onClick = { viewModel.conmutarGuardado(estado.evento) },
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                                )
                            ) {
                                Icon(
                                    imageVector = if (estado.isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                                    contentDescription = "Guardar",
                                    tint = if (estado.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (val estado = uiState) {
                is EventDetailUiState.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EventDetailUiState.Exito -> {
                    val evento = estado.evento
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box {
                            with(sharedTransitionScope) {
                                AsyncImage(
                                    model = evento.imageUrl,
                                    contentDescription = "Imagen de ${evento.title}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(360.dp)
                                        .sharedElement(
                                            rememberSharedContentState(key = "image_${evento.id}"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            // Gradiente superior para que se vean los iconos
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.4f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )
                        }
                        
                        GrecaTalavera()
                        
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = evento.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            InfoRow(icon = Icons.Rounded.CalendarToday, text = evento.date)
                            Spacer(modifier = Modifier.height(12.dp))
                            InfoRow(icon = Icons.Rounded.LocationOn, text = evento.location)

                            Spacer(modifier = Modifier.height(32.dp))

                            // Acciones principales
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Button(
                                    onClick = { 
                                        if (notificationPermissionState == null || notificationPermissionState.status.isGranted) {
                                            viewModel.programarRecordatorio(evento)
                                        } else {
                                            notificationPermissionState.launchPermissionRequest()
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                                ) {
                                    Icon(Icons.Rounded.NotificationsActive, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.btn_remind_me), fontWeight = FontWeight.Bold)
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                OutlinedButton(
                                    onClick = { 
                                        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(evento.location + ", Talavera de la Reina")}")
                                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                        mapIntent.setPackage("com.google.android.apps.maps")
                                        context.startActivity(mapIntent)
                                    },
                                    modifier = Modifier.weight(1f).height(56.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(Icons.Rounded.Map, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.btn_directions), fontWeight = FontWeight.Bold)
                                }
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 32.dp))
                            
                            Text(
                                text = "Sobre este evento",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = evento.description,
                                style = MaterialTheme.typography.bodyLarge,
                                lineHeight = 28.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding() + 40.dp))
                        }
                    }
                }
                is EventDetailUiState.Error -> {
                    EventDetailErrorContent(mensaje = estado.mensaje, onBack = onBack)
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon, 
                contentDescription = null, 
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text, 
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EventDetailErrorContent(mensaje: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Rounded.Error, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Text(mensaje, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}
