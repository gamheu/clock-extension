package com.supinfo.app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("current") val current: CurrentWeather,
    @SerializedName("daily") val daily: DailyWeather,
    @SerializedName("hourly") val hourly: HourlyWeather
)

data class CurrentWeather(
    @SerializedName("time") val time: String,
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("apparent_temperature") val apparentTemperature: Double,
    @SerializedName("weather_code") val weatherCode: Int,
    @SerializedName("wind_speed_10m") val windSpeed: Double,
    @SerializedName("wind_direction_10m") val windDirection: Int,
    @SerializedName("wind_gusts_10m") val windGusts: Double,
    @SerializedName("uv_index") val uvIndex: Double,
    @SerializedName("precipitation") val precipitation: Double,
    @SerializedName("is_day") val isDay: Int
)

data class DailyWeather(
    @SerializedName("time") val time: List<String>,
    @SerializedName("sunrise") val sunrise: List<String>,
    @SerializedName("sunset") val sunset: List<String>,
    @SerializedName("uv_index_max") val uvIndexMax: List<Double>,
    @SerializedName("precipitation_probability_max") val precipitationProbabilityMax: List<Int?>,
    @SerializedName("wind_speed_10m_max") val windSpeedMax: List<Double>,
    @SerializedName("wind_direction_10m_dominant") val windDirectionDominant: List<Int>
)

data class HourlyWeather(
    @SerializedName("time") val time: List<String>,
    @SerializedName("temperature_2m") val temperature: List<Double?>,
    @SerializedName("wind_speed_10m") val windSpeed: List<Double?>,
    @SerializedName("wind_direction_10m") val windDirection: List<Int?>,
    @SerializedName("weather_code") val weatherCode: List<Int?>
)
