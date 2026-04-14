package com.supinfo.app.util

fun weatherCodeToEmoji(code: Int): String = when (code) {
    0 -> "☀️"
    1 -> "🌤️"
    2 -> "⛅"
    3 -> "☁️"
    45, 48 -> "🌫️"
    51, 53, 55 -> "🌦️"
    56, 57 -> "🌧️"
    61, 63, 65 -> "🌧️"
    66, 67 -> "🌨️"
    71, 73, 75 -> "❄️"
    77 -> "🌨️"
    80, 81, 82 -> "🌦️"
    85, 86 -> "🌨️"
    95 -> "⛈️"
    96, 99 -> "⛈️"
    else -> "🌡️"
}

fun weatherCodeToDescription(code: Int): String = when (code) {
    0 -> "Clear sky"
    1 -> "Mainly clear"
    2 -> "Partly cloudy"
    3 -> "Overcast"
    45, 48 -> "Foggy"
    51, 53, 55 -> "Drizzle"
    56, 57 -> "Freezing drizzle"
    61, 63, 65 -> "Rain"
    66, 67 -> "Freezing rain"
    71, 73, 75 -> "Snowfall"
    77 -> "Snow grains"
    80, 81, 82 -> "Rain showers"
    85, 86 -> "Snow showers"
    95 -> "Thunderstorm"
    96, 99 -> "Thunderstorm with hail"
    else -> "Unknown"
}

/** Convert wind degrees to 16-point compass label */
fun degreesToCompass(degrees: Int): String {
    val dirs = listOf(
        "N", "NNE", "NE", "ENE",
        "E", "ESE", "SE", "SSE",
        "S", "SSW", "SW", "WSW",
        "W", "WNW", "NW", "NNW"
    )
    val index = ((degrees + 11.25) / 22.5).toInt() % 16
    return dirs[index]
}

/** Extract HH:MM from an ISO-8601 datetime string "2024-01-15T14:00" */
fun formatHourMinute(isoTime: String): String =
    if (isoTime.contains("T")) isoTime.substringAfter("T").take(5) else isoTime

fun uvIndexToRating(uvIndex: Double): String = when {
    uvIndex < 3 -> "Low"
    uvIndex < 6 -> "Moderate"
    uvIndex < 8 -> "High"
    uvIndex < 11 -> "Very High"
    else -> "Extreme"
}

/** Find the first hourly slot index >= current hour */
fun currentHourIndex(times: List<String>): Int {
    val nowHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val idx = times.indexOfFirst { t ->
        val h = t.substringAfter("T").substringBefore(":").toIntOrNull() ?: 0
        h >= nowHour
    }
    return if (idx < 0) 0 else idx
}
