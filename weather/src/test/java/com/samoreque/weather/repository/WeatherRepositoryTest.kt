package com.samoreque.weather.repository

import java.lang.Exception
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.samoreque.weather.utils.MainCoroutineScopeRule
import com.samoreque.weather.utils.WeatherDataUtils
import com.samoreque.weather.WeatherException
import com.samoreque.weather.models.Location
import com.samoreque.weather.models.WeatherData
import com.samoreque.weather.models.WeatherUnits
import com.samoreque.weather.network.NetworkRepository
import com.samoreque.weather.network.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class WeatherRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var weatherApi: WeatherApi


    private lateinit var networkRepository: NetworkRepository

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)
        networkRepository = NetworkRepository(weatherApi, Dispatchers.Main)

    }

    @Test
    fun `Should return the weather data when fetchWeatherConditions is called`() = runBlockingTest {
        //Arrange
        val weatherRequest = WeatherDataUtils.getWeatherRequest()
        val location = Location(0.0, 0.0)
        `when`(weatherApi.getWeather(any(), any(), any(), any())).thenReturn(weatherRequest)
        //Act
        val weatherData = networkRepository.fetchWeatherConditions(location, WeatherUnits.IMPERIAL)
        //Assert
        assertThat(weatherData).isEqualTo(WeatherData.from(weatherRequest))
    }

    @Test
    fun `Should throw a WeatherConditionException when fetchWeatherConditions is called with error`() =
        runBlockingTest {
            //Arrange
            val location = Location(0.0, 0.0)
            val exception = Exception("Error")
            var exceptionActual: Throwable? = null
            `when`(weatherApi.getWeather(any(), any(), any(), any())).thenAnswer {
                throw exception
            }
            //Act
            try {
                networkRepository.fetchWeatherConditions(location, WeatherUnits.IMPERIAL)
            } catch (throwable: Throwable) {
                exceptionActual = throwable
            }

            //Assert
            assertThat(exceptionActual is WeatherException.WeatherConditionException).isEqualTo(true)

        }
}