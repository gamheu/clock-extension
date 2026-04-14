package com.supinfo.app.data.model

import com.google.gson.annotations.SerializedName

data class MarineResponse(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("current") val current: CurrentMarine?
)

data class CurrentMarine(
    @SerializedName("time") val time: String,
    @SerializedName("wave_height") val waveHeight: Double?,
    @SerializedName("wave_direction") val waveDirection: Int?,
    @SerializedName("wave_period") val wavePeriod: Double?,
    @SerializedName("wind_wave_height") val windWaveHeight: Double?,
    @SerializedName("swell_wave_height") val swellWaveHeight: Double?
)
