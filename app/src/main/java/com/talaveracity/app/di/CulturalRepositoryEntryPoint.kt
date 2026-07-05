package com.talaveracity.app.di

import com.talaveracity.app.domain.repository.CulturalRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CulturalRepositoryEntryPoint {
    fun culturalRepository(): CulturalRepository
}
