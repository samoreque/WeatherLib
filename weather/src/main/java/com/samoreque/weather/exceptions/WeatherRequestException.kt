package com.samoreque.weather.exceptions

import java.lang.Exception
import java.lang.IllegalArgumentException

abstract class WeatherRequestException(throwable: Throwable) : Throwable(cause = throwable)
class WeatherConditionException(throwable: Throwable) : WeatherRequestException(throwable)


abstract class WeatherLocationException(throwable: Throwable) : Throwable(cause = throwable)
class WeatherLocationPermissionException(exception: SecurityException) :
    WeatherLocationException(exception)

class WeatherLocationProviderNotFoundException(exception: IllegalArgumentException) :
    WeatherLocationException(exception)

class WeatherLocationProvidersDisableException :
    WeatherLocationException(Exception("Location providers disabled "))
