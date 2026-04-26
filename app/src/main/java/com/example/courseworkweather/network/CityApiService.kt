package com.example.courseworkweather.network

import com.example.courseworkweather.data.City
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApiService {
    @GET("v1/city")
    suspend fun getCityCoordinates(
        @Query("name") cityName: String
    ): List<City>
}