package com.example.courseworkweather.repository

import android.content.Context
import com.example.courseworkweather.data.City
import com.example.courseworkweather.data.CityEntity
import com.example.courseworkweather.data.WeatherDatabase
import com.example.courseworkweather.data.WeatherResponse
import com.example.courseworkweather.network.ModExemplar
import java.io.IOException

class WeatherRepository(private val context: Context) {

    private val database = WeatherDatabase.getInstance(context)
    private val cityDao = database.cityDao()

    suspend fun getCityCoordinates(cityName: String): City? {
        // 1. Кэш
        val cached = cityDao.getCityByName(cityName)
        if (cached != null) {
            return City(cached.name, cached.latitude, cached.longitude)
        }

        // 2. Запрос к Open-Meteo Geocoding API
        val response = try {
            ModExemplar.geocodingApiService.searchLocation(cityName)
        } catch (e: Exception) {
            throw IOException("Ошибка сети при получении координат", e)
        }

        val city = response.results.firstOrNull()
        if (city != null) {
            cityDao.insertCity(CityEntity(city.name, city.latitude, city.longitude))
            return City(city.name, city.latitude, city.longitude)
        }
        return null
    }

    suspend fun searchCities(query: String): List<String> {
        if (query.length < 2) return emptyList()
        return try {
            val response = ModExemplar.geocodingApiService.searchLocation(query)
            response.results.map { it.name }.distinct()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse {
        return ModExemplar.weatherApiService.getWeather(latitude, longitude)
    }
}