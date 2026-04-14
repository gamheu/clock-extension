package com.supinfo.app.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val WEATHER_BASE_URL = "https://api.open-meteo.com/"
    private const val MARINE_BASE_URL = "https://marine-api.open-meteo.com/"
    private const val GEOCODING_BASE_URL = "https://geocoding-api.open-meteo.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private fun buildRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherApi: WeatherApiService =
        buildRetrofit(WEATHER_BASE_URL).create(WeatherApiService::class.java)

    val marineApi: MarineApiService =
        buildRetrofit(MARINE_BASE_URL).create(MarineApiService::class.java)

    val geocodingApi: GeocodingApiService =
        buildRetrofit(GEOCODING_BASE_URL).create(GeocodingApiService::class.java)
}
