package com.samoreque.weather.models

import com.samoreque.weather.network.models.WeatherRequest

data class WeatherData(
    val temperature: Double,
    val windChill: Double,
    val windSpeed: Double
) {
    companion object {
        internal fun from(weatherRequest: WeatherRequest) = WeatherData(
            weatherRequest.main.temperature,
            weatherRequest.main.feelsLike,
            weatherRequest.wind.speed
        )
    }
}