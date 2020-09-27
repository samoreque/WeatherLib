package com.samoreque.weather

sealed class WeatherException(throwable: Throwable) : Throwable(cause = throwable) {
    class WeatherConditionException(throwable: Throwable) : WeatherException(throwable)
}