package com.samoreque.weather.network


import com.samoreque.weather.WeatherException
import com.samoreque.weather.models.Location
import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.models.WeatherUnits
import com.samoreque.weather.network.api.WeatherApi
import com.samoreque.weather.network.api.WeatherService
import com.samoreque.weather.respository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Perform the operations for the weather provider form the network.
 * @property weatherApi perform the API request through Retrofit
 */
internal class NetworkRepository(
    private val weatherApi: WeatherApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {

    override suspend fun fetchWeatherConditions(location: Location, weatherUnits: WeatherUnits)
            : WeatherData {
        return withContext(dispatcher) {
            try {
                val weatherRequest =
                    weatherApi.getWeather(location.latitude, location.longitude, weatherUnits.value,
                        WeatherService.API_KEY)
                WeatherData.from(weatherRequest)
            } catch (e: Throwable) {
                throw WeatherException.WeatherCondition(e)
            }
        }
    }


}


