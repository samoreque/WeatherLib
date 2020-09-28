package com.samoreque.weather.models

import android.location.Location


data class WeatherLocation(val latitude: Double, val longitude: Double)

internal fun Location.toWeatherLocation() = WeatherLocation(this.latitude, this.longitude)