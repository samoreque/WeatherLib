package com.samoreque.weather.location

import android.webkit.ValueCallback
import com.samoreque.weather.models.WeatherLocation

internal interface WeatherLocationManager {
    fun getLocation(valueCallback: ValueCallback<WeatherLocation>)
}