package com.supinfo.app.data.repository

import com.supinfo.app.data.api.RetrofitClient
import com.supinfo.app.data.model.MarineResponse
import com.supinfo.app.data.model.WeatherResponse
import com.supinfo.app.domain.HourlyForecast
import com.supinfo.app.domain.SupConditions
import com.supinfo.app.domain.SupScoreCalculator
import com.supinfo.app.util.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SupRepository {

    private val weatherApi = RetrofitClient.weatherApi
    private val marineApi = RetrofitClient.marineApi
    private val geocodingApi = RetrofitClient.geocodingApi

    suspend fun getSupConditions(cityName: String): Result<SupConditions> = runCatching {
        // 1 — Geocode
        val geoResult = geocodingApi.searchCity(cityName.trim(), 1, "en", "json")
        val location = geoResult.results?.firstOrNull()
            ?: throw Exception("City not found: \"$cityName\"")

        val lat = location.latitude
        val lon = location.longitude

        // 2 — Fetch weather + marine in parallel
        val (weather, marine) = coroutineScope {
            val weatherDeferred = async {
                weatherApi.getForecast(
                    latitude = lat,
                    longitude = lon,
                    current = "temperature_2m,apparent_temperature,weather_code," +
                            "wind_speed_10m,wind_direction_10m,wind_gusts_10m," +
                            "uv_index,precipitation,is_day",
                    daily = "sunrise,sunset,uv_index_max," +
                            "precipitation_probability_max," +
                            "wind_speed_10m_max,wind_direction_10m_dominant",
                    hourly = "temperature_2m,wind_speed_10m,wind_direction_10m,weather_code",
                    windSpeedUnit = "kmh",
                    timezone = "auto",
                    forecastDays = 1
                )
            }
            val marineDeferred = async {
                try {
                    marineApi.getMarineForecast(
                        latitude = lat,
                        longitude = lon,
                        current = "wave_height,wave_direction,wave_period," +
                                "wind_wave_height,swell_wave_height",
                        timezone = "auto"
                    )
                } catch (_: Exception) {
                    null   // Not available for inland / non-coastal coordinates
                }
            }
            Pair(weatherDeferred.await(), marineDeferred.await())
        }

        // 3 — Map to domain model
        mapToSupConditions(
            cityName = location.name,
            countryCode = location.countryCode ?: "",
            lat = lat,
            lon = lon,
            weather = weather,
            marine = marine
        )
    }

    // ------------------------------------------------------------------
    private fun mapToSupConditions(
        cityName: String,
        countryCode: String,
        lat: Double,
        lon: Double,
        weather: WeatherResponse,
        marine: MarineResponse?
    ): SupConditions {
        val cur = weather.current
        val daily = weather.daily
        val hourly = weather.hourly

        val waveHeight = marine?.current?.waveHeight
        val supScore = SupScoreCalculator.calculate(
            windSpeed = cur.windSpeed,
            windGusts = cur.windGusts,
            waveHeight = waveHeight,
            weatherCode = cur.weatherCode
        )

        // Hourly forecast: 8 hours starting from the current hour
        val startIdx = currentHourIndex(hourly.time)
        val hourlyForecast = (startIdx until minOf(startIdx + 8, hourly.time.size)).map { i ->
            HourlyForecast(
                time = formatHourMinute(hourly.time[i]),
                temperature = hourly.temperature.getOrNull(i) ?: 0.0,
                windSpeed = hourly.windSpeed.getOrNull(i) ?: 0.0,
                weatherCode = hourly.weatherCode.getOrNull(i) ?: 0,
                weatherEmoji = weatherCodeToEmoji(hourly.weatherCode.getOrNull(i) ?: 0)
            )
        }

        return SupConditions(
            cityName = cityName,
            countryCode = countryCode,
            latitude = lat,
            longitude = lon,
            lastUpdated = formatHourMinute(cur.time),

            temperature = cur.temperature,
            apparentTemperature = cur.apparentTemperature,
            weatherCode = cur.weatherCode,
            weatherDescription = weatherCodeToDescription(cur.weatherCode),
            weatherEmoji = weatherCodeToEmoji(cur.weatherCode),
            isDay = cur.isDay == 1,

            windSpeed = cur.windSpeed,
            windDirection = cur.windDirection,
            windDirectionText = degreesToCompass(cur.windDirection),
            windGusts = cur.windGusts,
            maxWindSpeed = daily.windSpeedMax.firstOrNull() ?: cur.windSpeed,

            precipitationProbability = daily.precipitationProbabilityMax.firstOrNull() ?: 0,

            uvIndex = cur.uvIndex,
            uvIndexMax = daily.uvIndexMax.firstOrNull() ?: cur.uvIndex,
            uvRating = uvIndexToRating(cur.uvIndex),
            sunrise = formatHourMinute(daily.sunrise.firstOrNull() ?: ""),
            sunset = formatHourMinute(daily.sunset.firstOrNull() ?: ""),

            waveHeight = marine?.current?.waveHeight,
            waveDirection = marine?.current?.waveDirection,
            waveDirectionText = marine?.current?.waveDirection?.let { degreesToCompass(it) },
            wavePeriod = marine?.current?.wavePeriod,
            swellWaveHeight = marine?.current?.swellWaveHeight,

            supScore = supScore,
            supScoreLabel = SupScoreCalculator.toLabel(supScore),
            supTip = SupScoreCalculator.toTip(supScore, cur.windSpeed, cur.weatherCode),

            hourlyForecast = hourlyForecast
        )
    }
}
