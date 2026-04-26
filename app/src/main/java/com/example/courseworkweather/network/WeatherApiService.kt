package com.example.courseworkweather.network

import com.example.courseworkweather.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,wind_speed_10m,wind_direction_10m,precipitation_probability,pressure_msl,weathercode",
        @Query("forecast_days") forecastDays: Int = 16,
        @Query("timezone") timezone: String = "auto",
        @Query("windspeed_unit") windspeedUnit: String = "ms"
    ): WeatherResponse
}