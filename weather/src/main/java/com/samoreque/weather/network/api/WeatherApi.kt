package com.samoreque.weather.network.api

import com.samoreque.weather.network.models.ForecastResponse
import com.samoreque.weather.network.models.WeatherRequest
import com.samoreque.weather.network.models.historical.HistoricalResponse
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


    @GET("onecall")
    suspend fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("exclude") exclude: String,
        @Query("appid") appId: String
    ): ForecastResponse

    @GET("onecall/timemachine")
    suspend fun getHistorical(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("dt") timestamp: Long,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): HistoricalResponse



}