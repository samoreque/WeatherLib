package com.samoreque.weather.utils

import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.network.models.MainTemperature
import com.samoreque.weather.network.models.WeatherRequest
import com.samoreque.weather.network.models.Wind

internal object WeatherDataUtils {
    fun getWeatherData() = WeatherData.from(getWeatherRequest())

    fun getWeatherRequest() = WeatherRequest(
        getMain(),
        getWind()
    )

    private fun getMain() = MainTemperature(1L,98.0, 99.5)
    private fun getWind() = Wind(speed = 120.0)
}