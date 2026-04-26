package com.example.courseworkweather.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HourlyWeather(
    val time: String,
    val temperature: Double,
    val humidity: Int?,
    val windSpeed: Double?,
    val windDirection: Int?,
    val precipProb: Int?,
    val pressure: Double?,
    val weatherCode: Int?
) : Parcelable {
    val hour: String get() = time.substringAfter("T").substringBefore(":") + ":00"
    val date: String get() = time.substringBefore("T")

    val windDirectionText: String
        get() = when (windDirection) {
            null -> "—"
            in 0..22, in 338..360 -> "С"
            in 23..67 -> "СВ"
            in 68..112 -> "В"
            in 113..157 -> "ЮВ"
            in 158..202 -> "Ю"
            in 203..247 -> "ЮЗ"
            in 248..292 -> "З"
            in 293..337 -> "СЗ"
            else -> "—"
        }

    val weatherIcon: String
        get() = when (weatherCode) {
            in 0..0 -> "☀️"          // Ясно
            in 1..2 -> "🌤️"          // Преимущечно ясно
            in 3..3 -> "⛅"           // Переменная облачность
            in 45..48 -> "🌫️"        // Туман
            in 51..55 -> "🌦️"        // Морось
            in 56..57 -> "🌧️❄️"      // Ледяная морось
            in 61..65 -> "🌧️"        // Дождь
            in 66..67 -> "🌧️❄️"      // Ледяной дождь
            in 71..75 -> "❄️"         // Снег
            in 80..82 -> "🌧️💧"      // Ливень
            in 95..99 -> "⛈️"        // Гроза
            else -> when {
                temperature > 25 -> "☀️"
                temperature > 15 -> "🌤️"
                temperature > 5 -> "⛅"
                temperature > -5 -> "☁️"
                else -> "❄️"
            }
        }
    val pressureMmHg: String?
        get() = pressure?.let {
            // 1 гПа ≈ 0.75006 мм рт. ст. форматируем до одного знака после запятой
            val mmHgValue = it * 0.75006
            String.format("%.1f", mmHgValue)
        }
}