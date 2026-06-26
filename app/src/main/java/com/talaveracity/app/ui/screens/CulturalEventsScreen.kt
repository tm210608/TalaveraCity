package com.talaveracity.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.talaveracity.app.domain.model.CulturalEvent
import com.talaveracity.app.domain.model.PuntoInteres
import com.talaveracity.app.ui.components.GrecaTalavera
import com.talaveracity.app.ui.viewmodel.EventosUiState
import com.talaveracity.app.ui.viewmodel.EventsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun CulturalEventsScreen(
    viewModel: EventsViewModel,
    onExploreClick: () -> Unit,
    onScanClick: () -> Unit,
    onCollectionClick: () -> Unit,
    onEventClick: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(permissionState.status.isGranted) {
        if (permissionState.status.isGranted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        viewModel.actualizarUbicacion(LatLng(it.latitude, it.longitude))
                    }
                }
            } catch (e: SecurityException) {
                // Manejar error de seguridad
            }
        }
    }

    Scaffold(
        bottomBar = {
            // Navigation Bar con efecto Glass y respeto a NavigationBarsPadding
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(32.dp))
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    modifier = Modifier.height(72.dp)
                ) {
                    NavigationBarItem(
                        selected = true,
                        onClick = { },
                        icon = { Icon(Icons.Rounded.Home, contentDescription = null) },
                        label = { Text("Inicio") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = onExploreClick,
                        icon = { Icon(Icons.Rounded.Explore, contentDescription = null) },
                        label = { Text("Mapa") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = onScanClick,
                        icon = { 
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.QrCodeScanner, 
                                    contentDescription = "Escanear", 
                                    tint = Color.White
                                )
                            }
                        },
                        label = { }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = onCollectionClick,
                        icon = { Icon(Icons.Rounded.AutoAwesomeMotion, contentDescription = null) },
                        label = { Text("Colección") }
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (val estado = uiState) {
                is EventosUiState.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EventosUiState.Exito -> {
                    val listState = rememberLazyListState()
                    val headerHeight = 420.dp
                    
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding() + 100.dp)
                    ) {
                        item {
                            DimensionalHeroHeader(
                                listState = listState,
                                headerHeight = headerHeight,
                                onScanClick = onScanClick,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                        
                        item {
                            GrecaTalavera(modifier = Modifier.padding(vertical = 8.dp))
                        }

                        // Sección de Bandos/Avisos Críticos
                        if (estado.bandos.isNotEmpty()) {
                            item {
                                AnnouncementsBanner(bandos = estado.bandos)
                            }
                        }
                        
                        // Sección de Actualidad (Noticias RSS)
                        if (estado.noticiasOficiales.isNotEmpty()) {
                            item {
                                SectionHeader(title = "Actualidad Municipal")
                            }
                            item {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 20.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(estado.noticiasOficiales) { noticia ->
                                        NewsCard(
                                            noticia = noticia,
                                            onClick = { onEventClick(noticia.id) }
                                        )
                                    }
                                }
                            }
                        }
                        
                        item {
                            SectionHeader(title = "Experiencias Únicas")
                        }

                        items(estado.eventos) { evento ->
                            GlassEventCard(
                                event = evento,
                                isSaved = estado.idsEventosGuardados.contains(evento.id),
                                onSaveToggle = { viewModel.conmutarGuardadoEvento(evento) },
                                onClick = { onEventClick(evento.id) },
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                        
                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
                is EventosUiState.Error -> {
                    ErrorContent(mensaje = estado.mensaje, onRetry = { viewModel.recargarTodo() })
                }
            }

            // Banner de Acción Rápida (Context-Aware)
            if (uiState is EventosUiState.Exito) {
                val estado = uiState as EventosUiState.Exito
                AnimatedVisibility(
                    visible = estado.puntoCercano != null,
                    enter = slideInVertically { -it } + fadeIn(),
                    exit = slideOutVertically { -it } + fadeOut(),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(16.dp)
                ) {
                    estado.puntoCercano?.let { punto ->
                        ContextBanner(
                            punto = punto,
                            onAction = onScanClick,
                            onDismiss = { /* Opcional: silenciar por un tiempo */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContextBanner(
    punto: PuntoInteres,
    onAction: () -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(24.dp)),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = Color.White)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Estás cerca de ${punto.titulo}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "¿Quieres escanear cerámica aquí?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
            
            IconButton(onClick = onDismiss) {
                Icon(Icons.Rounded.Close, contentDescription = "Cerrar")
            }

            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = onAction,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Escanear", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun NewsCard(
    noticia: CulturalEvent,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .width(300.dp)
            .height(180.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = noticia.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = noticia.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            TextButton(
                onClick = { /* Navegar a detalle */ },
                modifier = Modifier.align(Alignment.End),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Leer noticia",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DimensionalHeroHeader(
    listState: LazyListState,
    headerHeight: androidx.compose.ui.unit.Dp,
    onScanClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val currentTime = remember { Calendar.getInstance() }
    val hour = currentTime.get(Calendar.HOUR_OF_DAY)
    
    val greeting = when (hour) {
        in 6..12 -> "¡Buenos días!"
        in 13..20 -> "¡Buenas tardes!"
        else -> "¡Buenas noches!"
    }

    // Cálculo de Parallax
    val scrollOffset = remember { derivedStateOf { listState.firstVisibleItemScrollOffset.toFloat() } }
    val parallaxOffset = scrollOffset.value * 0.5f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .clip(RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp))
    ) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1543783230-050414a6003b?auto=format&fit=crop&q=80&w=2000",
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = parallaxOffset
                },
            contentScale = ContentScale.Crop
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        ),
                        startY = 0f,
                        endY = 1200f
                    )
                )
        )

        // Botones superiores minimalistas - Ajustados para statusBarsPadding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            with(sharedTransitionScope) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .sharedElement(
                            rememberSharedContentState(key = "logo_ea"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "EA",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            IconButton(
                onClick = onScanClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f))
            ) {
                Icon(
                    Icons.Rounded.QrCodeScanner, 
                    contentDescription = null, 
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(32.dp)
        ) {
            Text(
                text = greeting,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 38.sp,
                    lineHeight = 44.sp
                ),
                color = Color.White,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Explora el alma de barro y cobalto de nuestra ciudad milenaria.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 24.sp
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GlassEventCard(
    event: CulturalEvent,
    isSaved: Boolean,
    onSaveToggle: () -> Unit,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                with(sharedTransitionScope) {
                    AsyncImage(
                        model = event.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .sharedElement(
                                rememberSharedContentState(key = "image_${event.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        contentScale = ContentScale.Crop
                    )
                }
                
                // Overlay gradiente para legibilidad
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 300f
                            )
                        )
                )

                // Fecha
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = event.date,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botón guardar
                FilledTonalIconButton(
                    onClick = onSaveToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                    )
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                        contentDescription = "Guardar",
                        tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.5).sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun AnnouncementsBanner(bandos: List<CulturalEvent>) {
    val bando = bandos.first() // Mostramos el más reciente
    Surface(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer,
        shape = RoundedCornerShape(16.dp),
        border = border(2.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Campaign, 
                contentDescription = null, 
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "AVISO MUNICIPAL",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = bando.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 28.dp, end = 28.dp, top = 36.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Explorar",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun ErrorContent(mensaje: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.ErrorOutline, 
            contentDescription = null, 
            modifier = Modifier.size(64.dp), 
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "¡Ups! Algo salió mal", style = MaterialTheme.typography.titleLarge)
        Text(text = mensaje, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}




