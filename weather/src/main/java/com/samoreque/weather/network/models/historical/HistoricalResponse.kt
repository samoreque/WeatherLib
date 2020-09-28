package com.samoreque.weather.network.models.historical

import com.samoreque.weather.network.models.MainTemperature

internal data class HistoricalResponse(
    val current: MainTemperature,
    val hourly: List<MainTemperature>
) {

    fun calculateTemperature(): Pair<Double, Double> {
        var min = Double.MAX_VALUE
        var max = Double.MIN_VALUE
        hourly.forEach {
            if(it.temperature > max) {
                max = it.temperature
            }
            if (it.temperature < min) {
                min = it.temperature
            }
        }
        return Pair(min, max)
    }
}