package com.example.courseworkweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courseworkweather.data.DailyWeather
import com.example.courseworkweather.data.HourlyWeather
import com.example.courseworkweather.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val days: List<DailyWeather>) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _uiState = MutableLiveData<WeatherUiState>()
    val uiState: LiveData<WeatherUiState> = _uiState

    private val _suggestions = MutableLiveData<List<String>>()
    val suggestions: LiveData<List<String>> = _suggestions

    private var searchJob: Job? = null

    fun searchCity(query: String) {
        // отменяем предыдущий поиск
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // debounce 500 мс
            val result = repository.searchCities(query)
            _suggestions.value = result
        }
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }

    fun loadWeather(cityName: String) {
        if (cityName.isBlank()) return
        _uiState.value = WeatherUiState.Loading
        viewModelScope.launch {
            try {
                val city = repository.getCityCoordinates(cityName)
                if (city == null) {
                    _uiState.value = WeatherUiState.Error("Город не найден")
                    return@launch
                }

                val weatherResponse = repository.getWeather(city.latitude, city.longitude)

                val timeList = weatherResponse.hourly.time
                val tempList = weatherResponse.hourly.temperature2m
                val humidityList = weatherResponse.hourly.humidity ?: List(timeList.size) { null }
                val windSpeedList = weatherResponse.hourly.windSpeed ?: List(timeList.size) { null }
                val windDirList = weatherResponse.hourly.windDirection ?: List(timeList.size) { null }
                val precipList = weatherResponse.hourly.precipProb ?: List(timeList.size) { null }
                val pressureList = weatherResponse.hourly.pressure?: List(timeList.size) { null }
                val weatherCodeList = weatherResponse.hourly.weatherCode ?: List(timeList.size) { null }

                val hourlyList = timeList.indices.map { i ->
                    HourlyWeather(
                        time = timeList[i],
                        temperature = tempList[i],
                        humidity = humidityList[i],
                        windSpeed = windSpeedList[i],
                        windDirection = windDirList[i],
                        precipProb = precipList[i],
                        pressure = pressureList[i],
                        weatherCode = weatherCodeList[i]
                    )
                }

                val daysMap = hourlyList.groupBy { it.date }
                val days = daysMap.entries.take(5).map { (date, list) ->
                    DailyWeather(date, list.sortedBy { it.time })
                }

                _uiState.value = WeatherUiState.Success(days)
            } catch (e: IOException) {
                _uiState.value = WeatherUiState.Error("Ошибка сети: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error("Ошибка: ${e.message}")
            }
        }
    }
}