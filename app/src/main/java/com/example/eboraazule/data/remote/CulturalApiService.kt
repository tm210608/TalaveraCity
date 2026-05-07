package com.example.eboraazule.data.remote

import retrofit2.http.GET

interface CulturalApiService {
    @GET("events")
    suspend fun getEvents(): List<CulturalEventDto>
}
