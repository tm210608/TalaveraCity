package com.talaveracity.app.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface CulturalApiService {
    @GET("events")
    suspend fun getEvents(): List<CulturalEventDto>

    @GET
    suspend fun getRssFeed(@Url url: String): ResponseBody
}




