package com.samoreque.weather.respository

import com.samoreque.weather.models.WeatherLocation
import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.models.WeatherUnits

/**
 * Defines the operations allowed to perform operations on Weather provider.
 */
interface WeatherRepository {
    suspend fun fetchWeatherConditions(location: WeatherLocation, weatherUnits: WeatherUnits): WeatherData
}