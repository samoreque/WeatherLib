Weather
=======
 Android weather lib to get relevant information from Openweathermap  provider.
![Sample](https://github.com/samoreque/WeatherLib/blob/master/weather_app.jpeg)
#### Installation

```gradle
dependencies {
...
   implementation 'com.samoreque.weather:weather:1.0.0'
}
```

```
repositories {
    maven{
	  url "https://samoreque.mycloudrepo.io/public/repositories/weather"
  }
}
```
#### How to use
The library is following the current android recommendation regarding MVVM architecture, thus it is designed to work under ViewModel classes.

Creating the a `weatherRequester`:
```kotlin
val weatherRequester: WeatherRequester
 = WeatherRequester.Builder()
    .provider(OpenWeatherMapProvider(apiKey = "bb731234...."))
    .weatherUnits(WeatherUnits.IMPERIAL)
    .build(applicationContext)
```
The api key should be got from [openweathermap](https://openweathermap.org/api) site

Attaching the requester to to a view model:
```kotlin
init {
    weatherRequester.attach(this)
}
```
##### Fetching current weather for a specific location
```kotlin
weatherRequest.fetchWeather(latitude, longitude, ValueCallback {
  if (it.isSuccess) {
        _weatherDataLiveData.value = it.getOrNull()
    } else {
        handleError(it.exceptionOrNull())
    }
})
```
##### Fetching current weather user location
Before to call this method you should request `ACCESS_FINE_LOCATION` permission according to [android permission](https://developer.android.com/training/permissions/requesting) suggetsions.

```kotlin
weatherRequest.fetchWeather(ValueCallback {
  if (it.isSuccess) {
        _weatherDataLiveData.value = it.getOrNull()
    } else {
        handleError(it.exceptionOrNull())
    }
})
```
#####  Return the high & low temperatures for any given date
This method has some restriction according to [onecall api restrictions](https://openweathermap.org/api/one-call-api#history)
```kotlin
weatherRequest.fetchTemperature(latitude, longitude,
date, ValueCallback {
  if (it.isSuccess) {
        _historical.value = it.getOrNull()
    } else {
        handleError(it.exceptionOrNull())
    }
})
```

#### License
```
Copyright 2020 Samuel Reque

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

#### Developer contact
Samuel Reque - Sr. Android developer

email: samo.reque@gmail.com

linkedIn: https://www.linkedin.com/in/samuel-reque-zambrana/
