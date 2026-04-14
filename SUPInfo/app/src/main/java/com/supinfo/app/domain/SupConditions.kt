package com.supinfo.app.domain

data class SupConditions(
    val cityName: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double,
    val lastUpdated: String,

    // Current weather
    val temperature: Double,
    val apparentTemperature: Double,
    val weatherCode: Int,
    val weatherDescription: String,
    val weatherEmoji: String,
    val isDay: Boolean,

    // Wind — most critical for SUP
    val windSpeed: Double,           // km/h
    val windDirection: Int,          // degrees
    val windDirectionText: String,   // N, NE, E …
    val windGusts: Double,           // km/h
    val maxWindSpeed: Double,        // daily max, km/h

    // Precipitation
    val precipitationProbability: Int,  // %

    // UV & sun
    val uvIndex: Double,
    val uvIndexMax: Double,
    val uvRating: String,
    val sunrise: String,
    val sunset: String,

    // Marine — null for inland cities
    val waveHeight: Double?,
    val waveDirection: Int?,
    val waveDirectionText: String?,
    val wavePeriod: Double?,
    val swellWaveHeight: Double?,

    // Overall SUP assessment
    val supScore: SupScore,
    val supScoreLabel: String,
    val supTip: String,

    // Hourly forecast (next 8 hours)
    val hourlyForecast: List<HourlyForecast>
)

data class HourlyForecast(
    val time: String,        // "14:00"
    val temperature: Double,
    val windSpeed: Double,
    val weatherCode: Int,
    val weatherEmoji: String
)

enum class SupScore {
    EXCELLENT, GOOD, MODERATE, DIFFICULT, DANGEROUS
}
