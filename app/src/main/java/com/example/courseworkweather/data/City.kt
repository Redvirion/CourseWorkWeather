package com.example.courseworkweather.data

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("name")
    val name: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)