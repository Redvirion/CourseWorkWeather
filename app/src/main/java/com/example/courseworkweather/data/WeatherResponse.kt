package com.example.courseworkweather.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("hourly")
    val hourly: Hourly
)

data class Hourly(
    @SerializedName("time")
    val time: List<String>,
    @SerializedName("temperature_2m")
    val temperature2m: List<Double>,
    @SerializedName("relative_humidity_2m")
    val humidity: List<Int>?,               // в процентах
    @SerializedName("wind_speed_10m")
    val windSpeed: List<Double>?,           // м/с
    @SerializedName("wind_direction_10m")
    val windDirection: List<Int>?,          // градусы
    @SerializedName("precipitation_probability")
    val precipProb: List<Int>?,              // вероятность осадков %
    @SerializedName("pressure_msl")
    val pressure: List<Double>?,
    @SerializedName("weathercode")
    val weatherCode: List<Int>?
)