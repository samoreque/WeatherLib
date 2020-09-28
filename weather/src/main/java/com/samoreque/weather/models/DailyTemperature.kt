package com.samoreque.weather.models

import com.google.gson.annotations.SerializedName
import com.samoreque.weather.network.models.Daily
import com.samoreque.weather.network.models.historical.HistoricalResponse

data class DailyTemperature(
    @SerializedName("dt")
    val timeStampEpoch: Long,
    val min: Double,
    val max: Double
) {
    companion object {
        internal fun from(daily: Daily) = DailyTemperature(
            daily.dt, daily.temp.min, daily.temp.max
        )

        internal fun from(historical: HistoricalResponse): DailyTemperature {
            val tempPair = historical.calculateTemperature()
            return DailyTemperature(historical.current.timestamp, tempPair.first, tempPair.second)
        }
    }
}
