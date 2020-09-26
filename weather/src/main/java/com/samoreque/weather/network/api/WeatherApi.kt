package com.samoreque.weather.network.api

import com.samoreque.weather.network.models.WeatherRequest
import retrofit2.http.GET
import retrofit2.http.Query

internal interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): WeatherRequest

}