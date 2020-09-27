package com.samoreque.weather

import android.content.Context
import android.webkit.ValueCallback
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.samoreque.weather.location.WeatherLocationManager
import com.samoreque.weather.location.WeatherLocationManagerImpl
import com.samoreque.weather.models.WeatherLocation
import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.models.WeatherUnits
import com.samoreque.weather.network.scopes.NetworkScope
import com.samoreque.weather.network.scopes.ViewModelScope
import com.samoreque.weather.providers.OpenWeatherMapProvider
import com.samoreque.weather.providers.WeatherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.Result

class WeatherRequester internal constructor(
    private var provider: WeatherProvider,
    private var units: WeatherUnits,
    private var locationManager: WeatherLocationManager,
    private var networkScope: NetworkScope? = null
) {

    /**
     * Gets the current weather information for the [latitude] and [longitude] and returns it through the [ValueCallback] callback
     */
    fun fetchWeather(
        latitude: Double, longitude: Double,
        callback: ValueCallback<Result<WeatherData>>) = request(callback) {
        provider.repository.fetchWeatherConditions(WeatherLocation(latitude, longitude), units)
    }

    /**
     * Fetch current weather based on the user location
     */
    fun fetchWeather(
        callback: ValueCallback<Result<WeatherData>>) {
        try {
            fetchWeatherFromUserLocation(callback)
        } catch (throwable: Throwable) {
            callback.onReceiveValue(Result.failure(throwable))
        }
    }

    private fun fetchWeatherFromUserLocation(callback: ValueCallback<Result<WeatherData>>) {
        val valueCallback = ValueCallback<WeatherLocation> { request(callback) {
            provider.repository.fetchWeatherConditions(it, units)
        } }
        locationManager.getLocation(valueCallback)
    }

    /**
     * Gets the current weather information.
     *
     * This is a suspend function so it will run under an [CoroutineScope]
     */
    suspend fun fetchWeather(latitude: Double, longitude: Double) =
        provider.repository.fetchWeatherConditions(WeatherLocation(latitude, longitude), units)

    private fun <T> request(callback: ValueCallback<Result<T>>, block: suspend () -> T) {
        networkScope?.scope?.let {
            it.launch {
                try {
                    val data = block()
                    callback.onReceiveValue(Result.success(data))
                } catch (throwable: Throwable) {
                    callback.onReceiveValue(Result.failure(throwable))
                }
            }
        }
    }

    /**
     * Indicates to the requester what scope it is attached.
     *
     * WARNING: Please call [dispose] when the [ViewModel] is cleared by [ViewModel.onCleared]
     */
    fun attach(viewModel: ViewModel) = apply {
        networkScope = ViewModelScope(viewModel)
    }

    fun dispose() {
        networkScope?.dispose()
    }

    class Builder {

        private var provider: WeatherProvider = OpenWeatherMapProvider
        private var units: WeatherUnits = WeatherUnits.IMPERIAL

        fun provider(provider: WeatherProvider) = apply { this.provider = provider }
        fun weatherUnits(units: WeatherUnits) = apply { this.units = units }

        fun build(context: Context): WeatherRequester {
            return WeatherRequester(provider, units, WeatherLocationManagerImpl(context))
        }
    }
}
