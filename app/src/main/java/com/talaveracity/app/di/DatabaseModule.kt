package com.talaveracity.app.di

import android.content.Context
import androidx.room.Room
import com.talaveracity.app.data.local.TalaveraCityDatabase
import com.talaveracity.app.data.local.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TalaveraCityDatabase {
        return Room.databaseBuilder(
            context,
            TalaveraCityDatabase::class.java,
            "TalaveraCity_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideEventDao(database: TalaveraCityDatabase): EventDao {
        return database.eventDao()
    }
}




