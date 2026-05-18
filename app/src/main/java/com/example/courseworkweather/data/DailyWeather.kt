package com.example.courseworkweather.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyWeather(
    val date: String,
    val hourlyWeather: List<HourlyWeather>
) : Parcelable {
    val formattedDate: String
        get() {
            val parts = date.split("-")
            return if (parts.size == 3) "${parts[2]}.${parts[1]}" else date
        }
}