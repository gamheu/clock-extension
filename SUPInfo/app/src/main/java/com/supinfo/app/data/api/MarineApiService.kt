package com.supinfo.app.data.api

import com.supinfo.app.data.model.MarineResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarineApiService {
    @GET("v1/marine")
    suspend fun getMarineForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String,
        @Query("timezone") timezone: String
    ): MarineResponse
}
