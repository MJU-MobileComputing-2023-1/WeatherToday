package com.example.weathertoday

import WeatherData
import android.content.Intent
import kotlinx.coroutines.*
import androidx.lifecycle.lifecycleScope
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.weathertoday.WeatherService.Companion.API_KEY
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: WeatherDatabase
    private lateinit var weatherService: WeatherService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = WeatherDatabase.getDatabase(this)
        weatherService = WeatherService.create()

        lifecycleScope.launch(Dispatchers.IO) {
            val locations = database.locationDao().getAllLocations()
            if (locations.isEmpty()) {
                navigateToAddLocation()
            } else {
                withContext(Dispatchers.Main) {
                    // Fetch the weather data for each location
                    for (location in locations) {
                        val weatherData = fetchWeatherData(location.latitude, location.longitude)
                        val weather =
                            weatherData.toWeather(location.id)  // Convert WeatherData to Weather
                        database.weatherDao().insertWeather(weather)

                        // Display weather info on the screen
                        val weatherInfo = findViewById<TextView>(R.id.weatherInfo)
                        weatherInfo.text =
                            weather.toString() // Replace this with whatever format you want to display the weather info
                    }
                }
            }
        }
    }

    private suspend fun fetchWeatherData(latitude: Double, longitude: Double): WeatherData {
        val response = withContext(Dispatchers.IO) {
            weatherService.getWeather(
                API_KEY,
                "JSON",
                10,
                1,
                getCurrentDate(),
                getCurrentTime(),
                latitude.toInt(),
                longitude.toInt()
            )
        }

        val sdfDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val sdfTime = SimpleDateFormat("HHmm", Locale.getDefault())
        val currentDate = sdfDate.format(Date())
        val currentTime = sdfTime.format(Date())

        var temperature: Double? = null
        var humidity: Double? = null
        var rainfall1h: Double? = null
        var eastWestWind: Double? = null
        var southNorthWind: Double? = null
        var precipitationType: Int? = null
        var windDirection: Double? = null
        var windSpeed: Double? = null

        for (item in response.body.items.item) {
            when (item.category) {
                "T1H" -> temperature = item.fcstValue
                "REH" -> humidity = item.fcstValue
                "RN1" -> rainfall1h = item.fcstValue
                "UUU" -> eastWestWind = item.fcstValue
                "VVV" -> southNorthWind = item.fcstValue
                "PTY" -> precipitationType = item.fcstValue.toInt()
                "VEC" -> windDirection = item.fcstValue
                "WSD" -> windSpeed = item.fcstValue
            }
        }

        return WeatherData(
            temperature = temperature ?: 0.0,
            humidity = humidity ?: 0.0,
            rainfall1h = rainfall1h ?: 0.0,
            skyStatus = "",  // API 응답에 skyStatus에 해당하는 데이터가 없으므로 임의로 빈 문자열을 설정합니다.
            eastWestWind = eastWestWind ?: 0.0,
            southNorthWind = southNorthWind ?: 0.0,
            precipitationType = precipitationType ?: 0,
            lightning = "",  // API 응답에 lightning에 해당하는 데이터가 없으므로 임의로 빈 문자열을 설정합니다.
            windDirection = windDirection?.toString() ?: "",
            windSpeed = windSpeed?.toString() ?: "",
            date = currentDate,
            timestamp = currentTime
        )
    }


    private fun getCurrentDate(): String {
        val sdfDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdfDate.format(Date())
    }

    private fun getCurrentTime(): String {
        val sdfTime = SimpleDateFormat("HH00", Locale.getDefault())
        return sdfTime.format(Date())
    }

    private fun navigateToAddLocation() {
        val addLocationIntent = Intent(this, AddLocationActivity::class.java)
        startActivity(addLocationIntent)
        finish()
    }
}
