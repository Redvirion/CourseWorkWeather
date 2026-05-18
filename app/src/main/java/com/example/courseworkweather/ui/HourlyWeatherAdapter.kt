package com.example.courseworkweather.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.courseworkweather.R
import com.example.courseworkweather.data.HourlyWeather
import com.example.courseworkweather.databinding.ItemHourlyForecastBinding

class HourlyWeatherAdapter : RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>() {
    private var items: List<HourlyWeather> = emptyList()

    class ViewHolder(private val binding: ItemHourlyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HourlyWeather) {
            val context = binding.root.context

            binding.timeText.text = item.hour
            binding.iconText.text = item.weatherIcon
            binding.tempText.text = item.temperature?.let { "${it}°C" } ?: "—°C"

            // Влажность
            binding.humidityText.text = item.humidity?.let { "💧 $it%" } ?: "💧 —"

            // Ветер: скорость + направление
            val windSpeedText = item.windSpeed?.let { "${it.toInt()} ${context.getString(R.string.wind_unit)}" } ?: "—"
            val windDirectionText = getWindDirectionText(context, item.windDirection)
            binding.windText.text = "🌬️ $windSpeedText $windDirectionText"

            // Давление
            binding.pressureText.text = item.pressureMmHg?.let { "🧭 $it ${context.getString(R.string.pressure_unit)}" } ?: "🧭 —"

            // Осадки
            binding.precipText.text = item.precipProb?.let { "☔ $it%" } ?: "☔ —"
        }

        private fun getWindDirectionText(context: android.content.Context, degrees: Int?): String {
            if (degrees == null) return context.getString(R.string.wind_dir_default)
            return when (degrees) {
                in 0..22, in 338..360 -> context.getString(R.string.wind_dir_n)
                in 23..67 -> context.getString(R.string.wind_dir_ne)
                in 68..112 -> context.getString(R.string.wind_dir_e)
                in 113..157 -> context.getString(R.string.wind_dir_se)
                in 158..202 -> context.getString(R.string.wind_dir_s)
                in 203..247 -> context.getString(R.string.wind_dir_sw)
                in 248..292 -> context.getString(R.string.wind_dir_w)
                in 293..337 -> context.getString(R.string.wind_dir_nw)
                else -> context.getString(R.string.wind_dir_default)
            }
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