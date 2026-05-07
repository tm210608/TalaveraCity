package com.example.eboraazule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eboraazule.data.repository.CulturalRepository

class ViewModelFactory(private val repository: CulturalRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EventsViewModel::class.java) -> EventsViewModel(repository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository) as T
            modelClass.isAssignableFrom(ExplorationViewModel::class.java) -> ExplorationViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
