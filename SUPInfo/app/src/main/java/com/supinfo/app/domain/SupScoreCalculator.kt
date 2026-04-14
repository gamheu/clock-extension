package com.supinfo.app.domain

/**
 * Calculates an overall SUP (Stand Up Paddle) suitability score from
 * wind, wave, and weather conditions.
 *
 * Weighting:
 *  - Wind speed  40 %  (primary factor — speed kills balance on a board)
 *  - Wind gusts  20 %  (sudden gusts are dangerous)
 *  - Wave height 20 %  (coastal riders; inland = 5/5 by default)
 *  - Weather     20 %  (fog / thunderstorm block the session)
 */
object SupScoreCalculator {

    fun calculate(
        windSpeed: Double,
        windGusts: Double,
        waveHeight: Double?,
        weatherCode: Int
    ): SupScore {
        val windScore = when {
            windSpeed < 10 -> 5.0
            windSpeed < 15 -> 4.0
            windSpeed < 20 -> 3.0
            windSpeed < 28 -> 2.0
            else -> 1.0
        }

        val gustScore = when {
            windGusts < 15 -> 5.0
            windGusts < 25 -> 4.0
            windGusts < 35 -> 3.0
            windGusts < 45 -> 2.0
            else -> 1.0
        }

        // Inland cities have no wave data — assume flat water
        val waveScore = waveHeight?.let {
            when {
                it < 0.3 -> 5.0
                it < 0.6 -> 4.0
                it < 1.0 -> 3.0
                it < 1.5 -> 2.0
                else -> 1.0
            }
        } ?: 5.0

        val weatherScore = when (weatherCode) {
            0 -> 5.0
            in 1..3 -> 4.5
            45, 48 -> 2.5    // Fog — dangerous, poor visibility
            in 51..55 -> 3.5 // Light drizzle
            in 56..57 -> 2.0 // Freezing drizzle
            in 61..65 -> 2.5 // Rain
            in 66..67 -> 1.5 // Freezing rain
            in 71..77 -> 1.5 // Snow
            in 80..82 -> 3.0 // Showers — manageable
            in 85..86 -> 1.5 // Snow showers
            in 95..99 -> 1.0 // Thunderstorm — NEVER paddle
            else -> 3.0
        }

        val total = windScore * 0.4 + gustScore * 0.2 + waveScore * 0.2 + weatherScore * 0.2

        return when {
            total >= 4.5 -> SupScore.EXCELLENT
            total >= 3.5 -> SupScore.GOOD
            total >= 2.5 -> SupScore.MODERATE
            total >= 1.5 -> SupScore.DIFFICULT
            else -> SupScore.DANGEROUS
        }
    }

    fun toLabel(score: SupScore): String = when (score) {
        SupScore.EXCELLENT -> "Excellent"
        SupScore.GOOD -> "Good"
        SupScore.MODERATE -> "Moderate"
        SupScore.DIFFICULT -> "Challenging"
        SupScore.DANGEROUS -> "Dangerous"
    }

    fun toEmoji(score: SupScore): String = when (score) {
        SupScore.EXCELLENT -> "🏄"
        SupScore.GOOD -> "🤙"
        SupScore.MODERATE -> "⚠️"
        SupScore.DIFFICULT -> "🌊"
        SupScore.DANGEROUS -> "⛔"
    }

    fun toTip(score: SupScore, windSpeed: Double, weatherCode: Int): String = when (score) {
        SupScore.EXCELLENT ->
            "Perfect flat-water conditions! Ideal for all skill levels. Go enjoy the session!"
        SupScore.GOOD ->
            "Good conditions. Suitable for intermediates. Keep an eye on gusts."
        SupScore.MODERATE ->
            "Moderate conditions. Stay close to shore. Experienced paddlers only."
        SupScore.DIFFICULT ->
            if (windSpeed > 25)
                "Strong wind (${windSpeed.toInt()} km/h). Expert paddlers only — stay close to shore."
            else if (weatherCode in 95..99)
                "Thunderstorm detected. Do not paddle — lightning risk."
            else
                "Challenging conditions. Only experts with safety gear should go out."
        SupScore.DANGEROUS ->
            "Dangerous conditions. Stay on shore today. Safety first!"
    }
}
