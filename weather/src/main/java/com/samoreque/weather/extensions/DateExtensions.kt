package com.samoreque.weather.extensions

import java.util.Date

internal fun Date.totalDaysOffset() = (this.time / (1000 * 60 * 60 *24)).toInt()

/**
 * Compares day differences with [dateToCompare].
 *
 * returns >=0 when the date is greater than [dateToCompare]
 * returns < 0 when the date is less than [dateToCompare]
 */
internal fun Date.daysDiff(dateToCompare: Date) = dateToCompare.totalDaysOffset() - this.totalDaysOffset()