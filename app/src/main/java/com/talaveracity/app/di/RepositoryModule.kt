package com.talaveracity.app.di

import com.talaveracity.app.data.repository.CulturalRepositoryImpl
import com.talaveracity.app.domain.repository.CulturalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCulturalRepository(
        culturalRepositoryImpl: CulturalRepositoryImpl
    ): CulturalRepository
}




