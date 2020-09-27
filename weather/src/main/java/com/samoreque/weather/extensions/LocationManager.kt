package com.samoreque.weather.extensions

import android.location.Location
import android.location.LocationManager
import java.lang.IllegalArgumentException

internal val LocationManager.isGPSEnabled
    get() = this.isProviderEnabled(LocationManager.GPS_PROVIDER)

internal val LocationManager.isNetworkEnabled
    get() = this.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

@Throws(SecurityException::class, IllegalArgumentException::class)
internal fun LocationManager.getLastKnownLocation(): Location? {
    val networkLocation = this.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    val gpsLocation = this.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    return when {
        gpsLocation != null -> gpsLocation
        networkLocation != null -> networkLocation
        else -> null
    }
}



