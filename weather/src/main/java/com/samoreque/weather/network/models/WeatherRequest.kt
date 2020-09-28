package com.samoreque.weather.network.models

internal data class WeatherRequest(
    val main: MainTemperature,
    val wind: Wind
)