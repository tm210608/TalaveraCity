package com.example.eboraazule.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Welcome : Route
    
    @Serializable
    data object LandscapeCeramic : Route

    @Serializable
    data object CeramicAccess : Route

    @Serializable
    data object CulturalEvents : Route
    
    @Serializable
    data object Exploration : Route

    @Serializable
    data object Escaneo : Route

    @Serializable
    data object Coleccion : Route

    @Serializable
    data class EventDetail(val eventId: String) : Route
}
