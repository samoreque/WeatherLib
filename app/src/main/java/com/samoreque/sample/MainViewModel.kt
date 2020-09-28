package com.samoreque.sample

import android.app.Application
import android.webkit.ValueCallback
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.samoreque.weather.WeatherRequester
import com.samoreque.weather.models.DailyTemperature
import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.models.WeatherUnits
import com.samoreque.weather.providers.OpenWeatherMapProvider
import java.util.*

/**
 * Handles the events
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherRequest: WeatherRequester = WeatherRequester.Builder()
        .provider(OpenWeatherMapProvider(apiKey = "bb731234b71d83138a84037d38749820"))
        .weatherUnits(WeatherUnits.IMPERIAL)
        .build(application.applicationContext)

    init {
        weatherRequest.attach(this)
    }

    private val _weatherDataLiveData = MutableLiveData<WeatherData>()

    val weatherDataLiveData: LiveData<WeatherData>
        get() = _weatherDataLiveData

    private val _temperature = MutableLiveData<DailyTemperature>()

    val temperature: LiveData<DailyTemperature>
        get() = _temperature

    private val _historical = MutableLiveData<DailyTemperature>()

    val historical: LiveData<DailyTemperature>
        get() = _historical

    private val _refreshSpinner = MutableLiveData<Boolean>()

    val refreshSpinner: LiveData<Boolean>
        get() = _refreshSpinner

    private val _error = MutableLiveData<Throwable>()

    val error: LiveData<Throwable>
        get() = _error

    fun fetchWeatherData() {
        _refreshSpinner.value = true
        weatherRequest.fetchWeather( ValueCallback {
            if(it.isSuccess) {
                _weatherDataLiveData.value = it.getOrNull()
            } else {
                handleError(it.exceptionOrNull())
            }
            _refreshSpinner.value = false
        })

    }

    fun fetchWeatherData(latitude: Double, longitude: Double) {
        _refreshSpinner.value = true
        weatherRequest.fetchWeather(latitude, longitude, ValueCallback {
            if (it.isSuccess) {
                _weatherDataLiveData.value = it.getOrNull()
            } else {
                handleError(it.exceptionOrNull())
            }

            _refreshSpinner.value = false
        })

    }

    fun fetchTemperature(latitude: Double, longitude: Double, date: Date) {
        weatherRequest.fetchTemperature(latitude, longitude,date, ValueCallback {
            if (it.isSuccess) {
                _temperature.value = it.getOrNull()
            } else {
                handleError(it.exceptionOrNull())
            }
        })

    }

    fun fetchHistorical(latitude: Double, longitude: Double, date: Date) {
        weatherRequest.fetchTemperature(latitude, longitude,date, ValueCallback {
            if (it.isSuccess) {
                _historical.value = it.getOrNull()
            } else {
                handleError(it.exceptionOrNull())
            }
        })
    }

    private fun handleError(throwable: Throwable?) {
        throwable?.let {
            _error.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        weatherRequest.dispose()
    }
}
