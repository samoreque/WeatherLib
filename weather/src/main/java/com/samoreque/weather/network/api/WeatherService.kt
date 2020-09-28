package com.samoreque.weather.network.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class WeatherService {
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private const val SERVICE_TAG = "API"

        @JvmStatic
        fun create(): WeatherApi {
            val logger =
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d(SERVICE_TAG, it) })
            logger.level = HttpLoggingInterceptor.Level.BASIC
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi::class.java)
        }
    }
}