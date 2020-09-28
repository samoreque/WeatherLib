package com.samoreque.weather.network

import com.samoreque.weather.exceptions.WeatherConditionException
import com.samoreque.weather.exceptions.WeatherTemperatureException
import com.samoreque.weather.exceptions.WeatherTemperatureRangeException
import com.samoreque.weather.extensions.daysDiff
import com.samoreque.weather.models.DailyTemperature
import com.samoreque.weather.models.WeatherLocation
import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.models.WeatherUnits
import com.samoreque.weather.network.api.WeatherApi
import com.samoreque.weather.respository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Perform the operations for the OpenWeather provider form the network.
 * @property weatherApi perform the API request through Retrofit
 */
internal class OpenWeatherRepository(
    private val weatherApi: WeatherApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiKey: String
) : WeatherRepository {

    override suspend fun fetchWeatherConditions(location: WeatherLocation, weatherUnits: WeatherUnits)
            : WeatherData {
        return withContext(dispatcher) {
            try {
                val weatherRequest =
                    weatherApi.getWeather(location.latitude, location.longitude, weatherUnits.value,
                        apiKey)
                WeatherData.from(weatherRequest)
            } catch (e: Throwable) {
                throw WeatherConditionException(e)
            }
        }
    }
    override suspend fun fetchTemperature(date: Date, location: WeatherLocation,
                                         weatherUnits: WeatherUnits): DailyTemperature {
        return withContext(dispatcher) {
            try {
                val temperature = when(val request = determinateRequest(date)) {
                    is TemperatureRequest.Historical -> fetchHistorical(request.timeStampEpoch, location, weatherUnits)
                    is TemperatureRequest.Forecast -> fetchForecast(request.offset, location, weatherUnits)
                }
                temperature
            } catch (e: Throwable) {
                throw WeatherTemperatureException(e)
            }
        }
    }

    private suspend fun fetchForecast(dayOffset: Int, location: WeatherLocation,
                                       weatherUnits: WeatherUnits): DailyTemperature {
        val forecastRequest =
            weatherApi.getForecast(
                location.latitude, location.longitude,
                weatherUnits.value, EXCLUDE, apiKey
            )
        val daily = forecastRequest.daily[dayOffset]
        return DailyTemperature.from(daily)
    }

    private suspend fun fetchHistorical(timeStampEpoch: Long, location: WeatherLocation,
                                         weatherUnits: WeatherUnits): DailyTemperature {
         val historical = weatherApi.getHistorical(location.latitude, location.longitude, timeStampEpoch,
             weatherUnits.value, apiKey)
        return DailyTemperature.from(historical)
    }

    private fun determinateRequest(dateTemperature: Date): TemperatureRequest {
        val diff = Date().daysDiff(dateTemperature)
        return when {
            diff in 0..7 -> TemperatureRequest.Forecast(diff)
            diff < 0 && diff > -6 -> TemperatureRequest.Historical(dateTemperature.time / 1000L)
            else -> throw WeatherTemperatureRangeException(
                "The date [${dateTemperature.time}] is out of the range support by the OpenWeatherProvider, diff=$diff")
        }
    }

    companion object {
        const val EXCLUDE = "hourly"
    }

}
private sealed class TemperatureRequest {
    data class Forecast(val offset: Int) : TemperatureRequest()
    data class Historical(val timeStampEpoch: Long) : TemperatureRequest()
}


