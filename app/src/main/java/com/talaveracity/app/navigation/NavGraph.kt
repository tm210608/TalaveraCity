package com.talaveracity.app.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.talaveracity.app.ui.screens.*
import com.talaveracity.app.ui.viewmodel.*

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TalaveraCityNavGraph(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val backStack = rememberNavBackStack(Route.Splash)
    
    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) },
            modifier = modifier,
            entryProvider = entryProvider {
                entry<Route.Splash> {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        SplashScreen(
                            onTimeout = { 
                                backStack.removeAt(0)
                                backStack.add(Route.CulturalEvents) 
                            },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this
                        )
                    }
                }
                
                entry<Route.CulturalEvents> {
                    val eventsViewModel: EventsViewModel = hiltViewModel()
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CulturalEventsScreen(
                            viewModel = eventsViewModel,
                            onExploreClick = { backStack.add(Route.Exploration) },
                            onScanClick = { backStack.add(Route.Escaneo) },
                            onCollectionClick = { backStack.add(Route.Coleccion) },
                            onEventClick = { id -> backStack.add(Route.EventDetail(id)) },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this
                        )
                    }
                }
                
                entry<Route.EventDetail> { key ->
                    val detailViewModel: EventDetailViewModel = hiltViewModel()
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        EventDetailScreen(
                            eventId = key.eventId,
                            viewModel = detailViewModel,
                            onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this
                        )
                    }
                }
                
                entry<Route.Exploration> {
                    val explorationViewModel: ExplorationViewModel = hiltViewModel()
                    ExplorationScreen(
                        viewModel = explorationViewModel,
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }

                // Rutas auxiliares mantenidas por compatibilidad pero fuera del flujo principal
                entry<Route.Welcome> {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        WelcomeScreen({}, this@SharedTransitionLayout, this)
                    }
                }
                entry<Route.LandscapeCeramic> { LandscapeCeramicScreen {} }
                entry<Route.CeramicAccess> { CeramicAccessScreen {} }
                entry<Route.Escaneo> {
                    val escaneoViewModel: EscaneoViewModel = hiltViewModel()
                    EscaneoScreen(viewModel = escaneoViewModel, onBack = { backStack.removeAt(backStack.size - 1) })
                }
                entry<Route.Coleccion> {
                    val coleccionViewModel: CollectionViewModel = hiltViewModel()
                    CollectionScreen(viewModel = coleccionViewModel, onBack = { backStack.removeAt(backStack.size - 1) })
                }
            }
        )
    }
}




