package com.example.courseworkweather.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.courseworkweather.data.HourlyWeather
import com.example.courseworkweather.databinding.ItemHourlyForecastBinding

class HourlyWeatherAdapter : RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>() {
    private var items: List<HourlyWeather> = emptyList()

    class ViewHolder(private val binding: ItemHourlyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HourlyWeather) {
            binding.timeText.text = item.hour
            binding.iconText.text = item.weatherIcon
            binding.tempText.text = "${item.temperature}°C"
            binding.humidityText.text = item.humidity?.let { "💧 $it%" } ?: "💧 —"
            binding.windText.text = item.windSpeed?.let { "🌬️ ${it.toInt()} м/с ${item.windDirectionText}" } ?: "🌬️ —"
            binding.pressureText.text = item.pressureMmHg?.let { "🧭 $it мм рт. ст." } ?: "🧭 —"
            binding.precipText.text = item.precipProb?.let { "☔ $it%" } ?: "☔ —"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHourlyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<HourlyWeather>) {
        items = newItems
        notifyDataSetChanged()
    }
}