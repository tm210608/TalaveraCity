package com.example.eboraazule.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.eboraazule.EboraApplication
import com.example.eboraazule.ui.screens.*
import com.example.eboraazule.ui.viewmodel.EventsViewModel
import com.example.eboraazule.ui.viewmodel.ExplorationViewModel
import com.example.eboraazule.ui.viewmodel.EscaneoViewModel
import com.example.eboraazule.ui.viewmodel.ProfileViewModel
import com.example.eboraazule.ui.viewmodel.ViewModelFactory

@Composable
fun EboraAzuleNavGraph(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appContainer = (context.applicationContext as EboraApplication).container
    val backStack = rememberNavBackStack(Route.Welcome)
    
    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) },
        modifier = modifier,
        entryProvider = entryProvider {
            entry<Route.Welcome> {
                WelcomeScreen(
                    onExplore = { backStack.add(Route.LandscapeCeramic) }
                )
            }
            entry<Route.LandscapeCeramic> {
                LandscapeCeramicScreen(
                    onNext = { backStack.add(Route.CeramicAccess) },
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                )
            }
            entry<Route.CeramicAccess> {
                CeramicAccessScreen(
                    onNext = { backStack.add(Route.CulturalEvents) },
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                )
            }
            entry<Route.CulturalEvents> {
                val eventsViewModel: EventsViewModel = viewModel(
                    factory = ViewModelFactory(appContainer.repository)
                )
                CulturalEventsScreen(
                    viewModel = eventsViewModel,
                    onProfileClick = { backStack.add(Route.UserProfile) },
                    onExploreClick = { backStack.add(Route.Exploration) },
                    onScanClick = { backStack.add(Route.Escaneo) }
                )
            }
            entry<Route.UserProfile> {
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ViewModelFactory(appContainer.repository)
                )
                UserProfileScreen(
                    viewModel = profileViewModel,
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                )
            }
            entry<Route.Exploration> {
                val explorationViewModel: ExplorationViewModel = viewModel(
                    factory = ViewModelFactory(appContainer.repository)
                )
                ExplorationScreen(
                    viewModel = explorationViewModel,
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                )
            }
            entry<Route.Escaneo> {
                val escaneoViewModel: EscaneoViewModel = viewModel(
                    factory = ViewModelFactory(appContainer.repository)
                )
                EscaneoScreen(
                    viewModel = escaneoViewModel,
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                )
            }
        }
    )
}
