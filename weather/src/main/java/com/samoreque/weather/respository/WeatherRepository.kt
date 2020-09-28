package com.samoreque.weather.respository

import com.samoreque.weather.models.DailyTemperature
import com.samoreque.weather.models.WeatherLocation
import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.models.WeatherUnits
import java.util.*

/**
 * Defines the operations allowed to perform operations on Weather provider.
 */
interface WeatherRepository {
    suspend fun fetchWeatherConditions(location: WeatherLocation, weatherUnits: WeatherUnits): WeatherData
    suspend fun fetchTemperature(date: Date,location: WeatherLocation, weatherUnits: WeatherUnits): DailyTemperature
}