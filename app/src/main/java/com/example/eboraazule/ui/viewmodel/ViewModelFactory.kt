package com.example.eboraazule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eboraazule.data.repository.CulturalRepository

class ViewModelFactory(
    private val repository: CulturalRepository,
    private val notificationHelper: com.example.eboraazule.util.NotificationHelper? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EventsViewModel::class.java) -> EventsViewModel(repository) as T
            modelClass.isAssignableFrom(ExplorationViewModel::class.java) -> ExplorationViewModel() as T
            modelClass.isAssignableFrom(EscaneoViewModel::class.java) -> EscaneoViewModel(repository) as T
            modelClass.isAssignableFrom(EventDetailViewModel::class.java) -> {
                EventDetailViewModel(repository, notificationHelper!!) as T
            }
            modelClass.isAssignableFrom(CollectionViewModel::class.java) -> {
                CollectionViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
