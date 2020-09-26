package com.samoreque.weather

sealed class WeatherException(throwable: Throwable) : Throwable(cause = throwable) {
    class WeatherCondition(throwable: Throwable) : WeatherException(throwable)
}