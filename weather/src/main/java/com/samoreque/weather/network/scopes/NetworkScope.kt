package com.samoreque.weather.network.scopes

import kotlinx.coroutines.CoroutineScope

internal interface NetworkScope {
    val scope: CoroutineScope?
    fun dispose()
}
