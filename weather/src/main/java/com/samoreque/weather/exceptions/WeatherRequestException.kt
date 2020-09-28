package com.samoreque.weather.exceptions

import java.lang.Exception
import java.lang.IllegalArgumentException

abstract class WeatherRequestException(message: String, throwable: Throwable) :
    Throwable(message = message, cause = throwable)

class WeatherConditionException(throwable: Throwable) :
    WeatherRequestException("Unable to get Weather information", throwable)

class WeatherTemperatureException(throwable: Throwable) : WeatherRequestException("Unable to get Temperature information",throwable)
class WeatherTemperatureRangeException(message: String) :
    WeatherRequestException(message, Exception(message))


abstract class WeatherLocationException(throwable: Throwable) : Throwable(cause = throwable)
class WeatherLocationPermissionException(exception: SecurityException) :
    WeatherLocationException(exception)

class WeatherLocationProviderNotFoundException(exception: IllegalArgumentException) :
    WeatherLocationException(exception)

class WeatherLocationProvidersDisableException :
    WeatherLocationException(Exception("Location providers disabled "))
