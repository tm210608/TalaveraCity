package com.example.eboraazule.di

import android.content.Context
import androidx.room.Room
import com.example.eboraazule.data.local.EboraDatabase
import com.example.eboraazule.data.remote.CulturalApiService
import com.example.eboraazule.data.repository.CulturalRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AppContainer(private val context: Context) {

    private val database: EboraDatabase by lazy {
        Room.databaseBuilder(
            context,
            EboraDatabase::class.java,
            "ebora_database"
        ).build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.example.com/") // Placeholder
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val apiService: CulturalApiService by lazy {
        retrofit.create(CulturalApiService::class.java)
    }

    val repository: CulturalRepository by lazy {
        CulturalRepository(apiService, database.eventDao())
    }
}
