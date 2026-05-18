package com.example.courseworkweather.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    val name: String,          // название города – первичный ключ
    val latitude: Double,
    val longitude: Double
)