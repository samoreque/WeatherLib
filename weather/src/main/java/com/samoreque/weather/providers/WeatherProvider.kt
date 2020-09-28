package com.samoreque.weather.providers


import com.samoreque.weather.network.OpenWeatherRepository
import com.samoreque.weather.network.api.WeatherService
import com.samoreque.weather.respository.WeatherRepository

/**
 * Defines the different providers to be supported.
 */
abstract class WeatherProvider(open val repository: WeatherRepository)


data class OpenWeatherMapProvider(val apiKey: String) :
    WeatherProvider(repository = OpenWeatherRepository(WeatherService.create(), apiKey = apiKey))