package com.example.courseworkweather

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.courseworkweather.data.DailyWeather
import com.example.courseworkweather.databinding.ActivityMainBinding
import com.example.courseworkweather.ui.DayPagerAdapter
import com.example.courseworkweather.viewmodel.WeatherUiState
import com.example.courseworkweather.viewmodel.WeatherViewModel
import com.example.courseworkweather.viewmodel.WeatherViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(applicationContext)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var cityAdapter: ArrayAdapter<String>
    private var daysList: List<DailyWeather> = emptyList()
    companion object {
        private const val KEY_VIEWPAGER_POSITION = "viewpager_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // настройка отступов для краёв экрана
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // адаптер для подсказок
        cityAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ArrayList())
        binding.cityAutocomplete.setAdapter(cityAdapter)

        // изменения текста для поиска городов
        binding.cityAutocomplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.length >= 2) {
                    viewModel.searchCity(query)
                } else {
                    viewModel.clearSuggestions()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Обработка выбора подсказки
        binding.cityAutocomplete.setOnItemClickListener { _, _, position, _ ->
            val selectedCity = cityAdapter.getItem(position)
            if (selectedCity != null) {
                viewModel.loadWeather(selectedCity)
                // Скрыть клавиатуру
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.cityAutocomplete.windowToken, 0)
            }
        }

        // обновляем выпадающий список с проверкой активности Activity
        viewModel.suggestions.observe(this) { suggestions ->
            cityAdapter.clear()
            cityAdapter.addAll(suggestions)
            cityAdapter.notifyDataSetChanged()
            if (suggestions.isNotEmpty()) {
                // Проверяем, что Activity активна и не разрушена
                if (!isFinishing && !isDestroyed && lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    binding.cityAutocomplete.post {
                        if (!isFinishing && !isDestroyed) {
                            binding.cityAutocomplete.showDropDown()
                        }
                    }
                }
            }
        }

        // Кнопка "Погода" загружает прогноз по введённому тексту
        binding.refreshButton.setOnClickListener {
            val city = binding.cityAutocomplete.text.toString()
            if (city.isNotBlank()) {
                viewModel.loadWeather(city)
            }
        }

        // Обработка смены страницы
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (daysList.isNotEmpty() && position < daysList.size) {
                    binding.dateText.text = daysList[position].formattedDate
                }
            }
        })

        // наблюдение за состоянием прогноза
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is WeatherUiState.Loading -> {
                    Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_SHORT).show()
                }
                is WeatherUiState.Success -> {
                    daysList = state.days
                    val adapter = DayPagerAdapter(this, daysList)
                    binding.viewPager.adapter = adapter
                    if (daysList.isNotEmpty()) {
                        // Восстанавливаем сохранённую позицию, если есть
                        val savedPosition = savedInstanceState?.getInt(KEY_VIEWPAGER_POSITION, 0) ?: 0
                        val position = if (savedPosition < daysList.size) savedPosition else 0
                        binding.viewPager.setCurrentItem(position, false)
                        binding.dateText.text = daysList[position].formattedDate
                    }
                }
                is WeatherUiState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущую позицию ViewPager2, если адаптер уже установлен
        if (::binding.isInitialized && binding.viewPager.adapter != null) {
            outState.putInt(KEY_VIEWPAGER_POSITION, binding.viewPager.currentItem)
        }
    }
}