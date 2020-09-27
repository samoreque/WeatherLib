package com.samoreque.weather.respository

import com.samoreque.weather.models.Location
import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.models.WeatherUnits

/**
 * Defines the operations allowed to perform operations on Weather provider.
 */
interface WeatherRepository {
    suspend fun fetchWeatherConditions(location: Location, weatherUnits: WeatherUnits): WeatherData
}