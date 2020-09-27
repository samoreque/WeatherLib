package com.samoreque.weather

import android.webkit.ValueCallback
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.capture
import com.samoreque.weather.exceptions.WeatherConditionException
import com.samoreque.weather.exceptions.WeatherLocationPermissionException
import com.samoreque.weather.location.WeatherLocationManager
import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.models.WeatherLocation
import com.samoreque.weather.models.WeatherUnits
import com.samoreque.weather.providers.WeatherProvider
import com.samoreque.weather.respository.WeatherRepository
import com.samoreque.weather.utils.MainCoroutineScopeRule
import com.samoreque.weather.utils.WeatherDataUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.lang.Exception


@ExperimentalCoroutinesApi
class WeatherRequesterTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()
    private lateinit var viewModel: ViewModel

    @Mock
    private lateinit var weatherProvider: WeatherProvider

    @Mock
    private lateinit var weatherRepository: WeatherRepository

    @Mock
    private lateinit var weatherLocationManager: WeatherLocationManager

    @Mock
    private lateinit var valueCallback: ValueCallback<Result<WeatherData>>

    @Captor
    private lateinit var resultCaptor: ArgumentCaptor<Result<WeatherData>>

    @Captor
    private lateinit var locationCaptor: ArgumentCaptor<ValueCallback<WeatherLocation>>

    private lateinit var weatherRequester: WeatherRequester

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)
        viewModel = object : ViewModel() {}
        weatherRequester =
            WeatherRequester(weatherProvider, WeatherUnits.IMPERIAL, weatherLocationManager)
        weatherRequester.attach(viewModel)
        `when`(weatherProvider.repository).thenReturn(weatherRepository)

    }

    @Test
    fun `Should return WeatherData form callback when fetchWeather is called`() = runBlockingTest {
        //Arrange
        val weatherData = WeatherDataUtils.getWeatherData()
        `when`(weatherRepository.fetchWeatherConditions(any(), any())).thenReturn(weatherData)

        //Act
        weatherRequester.fetchWeather(0.0, 0.0, valueCallback)

        //Assert
        verify(valueCallback).onReceiveValue(resultCaptor.capture())
        assertThat(resultCaptor.value).isEqualTo(Result.success(weatherData))
    }

    @Test
    fun `Should throw a WeatherConditionException when fetchWeather is called with error`() =
        runBlockingTest {
            `when`(weatherRepository.fetchWeatherConditions(any(), any())).thenAnswer {
                throw WeatherConditionException(Exception())
            }

            //Act
            weatherRequester.fetchWeather(0.0, 0.0, valueCallback)

            //Assert
            verify(valueCallback).onReceiveValue(resultCaptor.capture())
            assertThat(resultCaptor.value.getOrNull()).isEqualTo(null)
            assertThat(
                resultCaptor.value.exceptionOrNull()
                        is WeatherConditionException
            ).isEqualTo(true)
        }

    @Test
    fun `Should return WeatherData form callback when fetchWeather with specific location is called`() = runBlockingTest {
        //Arrange
        val weatherData = WeatherDataUtils.getWeatherData()
        `when`(weatherRepository.fetchWeatherConditions(any(), any())).thenReturn(weatherData)

        //Act
        weatherRequester.fetchWeather(valueCallback)
        verify(weatherLocationManager).getLocation(capture(locationCaptor))

        locationCaptor.value.onReceiveValue(WeatherLocation(0.0, 0.0))

        //Assert
        verify(valueCallback).onReceiveValue(resultCaptor.capture())
        assertThat(resultCaptor.value).isEqualTo(Result.success(weatherData))
    }

    @Test
    fun `Should throw WeatherConditionException when fetchWeather with specific location failed`() =
        runBlockingTest {
            //Arrange
            val weatherData = WeatherDataUtils.getWeatherData()
            `when`(weatherRepository.fetchWeatherConditions(any(), any())).thenReturn(weatherData)
            `when`(weatherLocationManager.getLocation(any())).thenAnswer {
                throw WeatherLocationPermissionException(SecurityException())
            }

            //Act
            weatherRequester.fetchWeather(valueCallback)

            //Assert
            verify(valueCallback).onReceiveValue(resultCaptor.capture())
            assertThat(resultCaptor.value.exceptionOrNull() is WeatherLocationPermissionException)
                .isEqualTo(true)
        }

}