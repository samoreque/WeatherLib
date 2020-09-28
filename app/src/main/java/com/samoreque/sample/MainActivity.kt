package com.samoreque.sample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.samoreque.weather.models.DailyTemperature
import com.samoreque.weather.models.WeatherData
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel =
            ViewModelProvider.AndroidViewModelFactory(application).create(MainViewModel::class.java)


        viewModel.weatherDataLiveData.observe(this, Observer {
            showWeatherData(it)
        })
        viewModel.refreshSpinner.observe(this, Observer {
            enableProgressBar(it)
        })

        viewModel.temperature.observe(this, Observer {
            showTemperature(it)
        })

        viewModel.historical.observe(this, Observer {
            showTemperatureHistorical(it)
        })
        viewModel.error.observe(this, Observer {
            handleError(it)
        })

    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestWeatherInfo()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun showWeatherData(weatherData: WeatherData) {
        Log.d("Weather data", weatherData.toString())
        currentWeather.text = weatherData.toString()
    }

    private fun showTemperature(dailyTemperature: DailyTemperature) {
        Log.d("temperature", dailyTemperature.toString())
        temperature.text = dailyTemperature.toString()
    }

    private fun showTemperatureHistorical(dailyTemperature: DailyTemperature) {
        Log.d("temperature", dailyTemperature.toString())
        temperatureHistorical.text = dailyTemperature.toString()
    }

    private fun enableProgressBar(enabled: Boolean) {
        progressBar.visibility = if (enabled) View.VISIBLE else View.GONE
        currentWeather.visibility = if (!enabled) View.VISIBLE else View.GONE
    }

    private fun handleError(throwable: Throwable) {
        Toast.makeText(this, throwable.message, Toast.LENGTH_SHORT).show()
    }

    private fun requestWeatherInfo() {
        val forecastDate = Calendar.getInstance().run {
            add(Calendar.DAY_OF_MONTH, 1)
            time
        }
        val historicalDate = Calendar.getInstance().run {
            add(Calendar.DAY_OF_MONTH, -1)
            time
        }
        viewModel.fetchWeatherData(-17.784552, -63.182458)
        viewModel.fetchTemperature(-17.784552, -63.182458, forecastDate)
        viewModel.fetchHistorical(-17.784552, -63.182458, historicalDate)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    requestWeatherInfo()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            else -> {

            }
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 101
    }

}