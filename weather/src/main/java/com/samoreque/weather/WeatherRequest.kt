package com.samoreque.weather

import android.webkit.ValueCallback
import androidx.lifecycle.ViewModel
import com.samoreque.weather.models.Location
import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.models.WeatherUnits
import com.samoreque.weather.network.scopes.NetworkScope
import com.samoreque.weather.network.scopes.ViewModelScope
import com.samoreque.weather.providers.WeatherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.Result

class WeatherRequest private constructor(
    private var provider: WeatherProvider,
    private var units: WeatherUnits,
    private var networkScope: NetworkScope? = null
) {

    fun fetchWeather(latitude: Double, longitude: Double,
                     callback: ValueCallback<Result<WeatherData>>) = request(callback) {
        provider.repository.fetchWeatherConditions(Location(latitude, longitude), units)
    }

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
    fun attach(viewModel: ViewModel) = apply{
        networkScope = ViewModelScope(viewModel)
    }

    fun dispose() {
        networkScope?.dispose()
    }

    class Builder  {

        private var provider: WeatherProvider = WeatherProvider.OpenWeatherMapProvider
        private var units: WeatherUnits = WeatherUnits.IMPERIAL

        fun provider(provider: WeatherProvider) = apply { this.provider = provider }
        fun weatherUnits(units: WeatherUnits) = apply { this.units = units }

        fun build(): WeatherRequest {
            return WeatherRequest(provider, units)
        }
    }
}
