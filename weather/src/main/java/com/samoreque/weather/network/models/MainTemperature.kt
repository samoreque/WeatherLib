package com.samoreque.weather.network.models

import com.google.gson.annotations.SerializedName

internal data class MainTemperature(
    @SerializedName("dt")
    val timestamp: Long,
    @SerializedName("temp")
    val temperature: Double,

    @SerializedName("feels_like")
    val feelsLike: Double
)