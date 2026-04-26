package com.example.courseworkweather.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ModExemplar {

    // Open-Meteo Weather API
    private val openMeteoClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val openMeteoRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(openMeteoClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Open-Meteo Geocoding API
    private val geocodingRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://geocoding-api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //сервис для поиска городов
    val geocodingApiService: GeocodingApiService by lazy {
        geocodingRetrofit.create(GeocodingApiService::class.java)
    }

    // сервис для прогноза погоды
    val weatherApiService: WeatherApiService by lazy {
        openMeteoRetrofit.create(WeatherApiService::class.java)
    }
}