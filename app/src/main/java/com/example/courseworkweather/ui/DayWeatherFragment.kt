package com.example.courseworkweather.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.courseworkweather.data.DailyWeather
import com.example.courseworkweather.databinding.FragmentDayForecastBinding


class DayWeatherFragment : Fragment() {
    private var _binding: FragmentDayForecastBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dailyWeather = arguments?.getParcelable<DailyWeather>("dailyForecast")
        if (dailyWeather != null) {
            val adapter = HourlyWeatherAdapter()
            val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(requireContext(), 2) // две колонки
            } else {
                LinearLayoutManager(requireContext())
            }
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = adapter
            adapter.updateData(dailyWeather.hourlyWeather)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dailyWeather: DailyWeather): DayWeatherFragment {
            return DayWeatherFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("dailyForecast", dailyWeather)
                }
            }
        }
    }
}