package com.samoreque.weather.providers

import com.samoreque.weather.network.NetworkRepository
import com.samoreque.weather.network.api.WeatherService
import com.samoreque.weather.respository.WeatherRepository

/**
 * Defines the different providers to be supported.
 */
sealed class WeatherProvider(internal val repository: WeatherRepository) {

    object OpenWeatherMapProvider :
        WeatherProvider(repository = NetworkRepository(WeatherService.create()))
}