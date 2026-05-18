package com.example.courseworkweather.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.courseworkweather.data.DailyWeather

class DayPagerAdapter(
    activity: FragmentActivity,
    private val days: List<DailyWeather>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = days.size

    fun getItem(position: Int): DailyWeather = days[position]

    override fun createFragment(position: Int): Fragment {
        return DayWeatherFragment.newInstance(days[position])
    }
}