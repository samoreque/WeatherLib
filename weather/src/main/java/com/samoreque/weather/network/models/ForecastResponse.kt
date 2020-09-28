package com.samoreque.weather.network.models

internal data class ForecastResponse(
    val daily: List<Daily>
)