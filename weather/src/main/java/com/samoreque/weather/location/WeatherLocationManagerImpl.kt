package com.samoreque.weather.location

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import android.webkit.ValueCallback
import com.samoreque.weather.exceptions.WeatherLocationPermissionException
import com.samoreque.weather.exceptions.WeatherLocationProviderNotFoundException
import com.samoreque.weather.exceptions.WeatherLocationProvidersDisableException
import com.samoreque.weather.extensions.getLastKnownLocation
import com.samoreque.weather.extensions.isGPSEnabled
import com.samoreque.weather.extensions.isNetworkEnabled
import com.samoreque.weather.models.WeatherLocation
import com.samoreque.weather.models.toWeatherLocation

internal class WeatherLocationManagerImpl(context: Context) : WeatherLocationManager,
    WeatherLocationListener {
    private lateinit var valueCallback: ValueCallback<WeatherLocation>
    private val locationManager: LocationManager? = context.getSystemService(LOCATION_SERVICE) as? LocationManager

    override fun getLocation(valueCallback: ValueCallback<WeatherLocation>) {
        this.valueCallback = valueCallback
        try {
            locationManager?.let { safeLocationManager ->
                if (!safeLocationManager.isGPSEnabled && !safeLocationManager.isNetworkEnabled) {
                    throw WeatherLocationProvidersDisableException()
                }
                val location = safeLocationManager.getLastKnownLocation()
                if (location != null) {
                    valueCallback.onReceiveValue(location.toWeatherLocation())
                } else {
                    safeLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            throw WeatherLocationPermissionException(e)

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            throw WeatherLocationProviderNotFoundException(e)
        }
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        location?.let {
            valueCallback.onReceiveValue(it.toWeatherLocation())
            locationManager?.removeUpdates(this)
        }
    }

    companion object {

        // The minimum distance to change Updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 0f

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES =  0L// 1 minute
    }

}