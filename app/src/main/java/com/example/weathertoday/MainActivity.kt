package com.example.weathertoday

import WeatherData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: WeatherDatabase

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst/") // Replace with your API base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherService = retrofit.create(WeatherService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = WeatherDatabase.getDatabase(this)

        // Check if the location is stored
        val sharedPreferences = getSharedPreferences("WeatherToday", Context.MODE_PRIVATE)
        val locationId = sharedPreferences.getInt("selectedLocationId", -1)
        if (locationId == -1) {
            // If no location is stored, start AddLocation activity
            val intent = Intent(this, AddLocationActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        lifecycleScope.launch(Dispatchers.IO) {
            val location = database.locationDao().getLocation(locationId)
            withContext(Dispatchers.Main) {
                // Trigger the API call
                fetchWeather(location)
                displayLocationInfo(location)
            }
        }
    }

    private fun fetchWeather(location: Location) {
        val serviceKey =
            "Rj7N4ncTPAmKly7pqUSlvLBtqd1spygWPYqifDXUEothQB9qx1AbuKtl4oitCvYHxTrlri4P9TDfsx%2BKw670UQ%3D%3D"
        val numOfRows = 10
        val pageNo = 1
        val base_date = getCurrentDate()
        val base_time = getCurrentTime()
        val nx = location.latitude.toInt()  // replace with the actual calculation
        val ny = location.longitude.toInt()  // replace with the actual calculation

        lifecycleScope.launch {
            val response = weatherService.getCurrentWeather(
                serviceKey,
                numOfRows,
                pageNo,
                base_date,
                base_time,
                nx,
                ny
            )
            val weatherItems = response.response.body.items.item

            // Parse and map the API response to your WeatherData model
            val weatherData = mapApiResponseToWeatherData(weatherItems)

            // Update your UI here with the new weather data
            updateWeatherInfo(weatherData)
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HHmm", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun mapApiResponseToWeatherData(items: List<WeatherDataItem>): WeatherData {
        var temperature: Double? = null
        var rain: Double? = null
        var windEw: Double? = null
        var windSn: Double? = null
        var humidity: Double? = null
        var precipitation: Double? = null
        var windDirection: Double? = null
        var windSpeed: Double? = null

        for (item in items) {
            when (item.category) {
                "T1H" -> temperature = item.fcstValue.toDouble()
                "RN1" -> rain = item.fcstValue.toDouble()
                "UUU" -> windEw = item.fcstValue.toDouble()
                "VVV" -> windSn = item.fcstValue.toDouble()
                "REH" -> humidity = item.fcstValue.toDouble()
                "PTY" -> precipitation = item.fcstValue.toDouble()
                "VEC" -> windDirection = item.fcstValue.toDouble()
                "WSD" -> windSpeed = item.fcstValue.toDouble()
            }
        }
        return WeatherData(
            temperature,
            rain,
            windEw,
            windSn,
            humidity,
            precipitation,
            windDirection,
            windSpeed
        )
    }

    private fun updateWeatherInfo(data: WeatherData) {
        val weatherInfo = findViewById<TextView>(R.id.weatherInfo)

        weatherInfo.text = "Temperature: ${data.temperature}\n" +
                "Rain: ${data.rain}\n" +
                "Wind EW Component: ${data.windEw}\n" +
                "Wind SN Component: ${data.windSn}\n" +
                "Humidity: ${data.humidity}\n" +
                "Precipitation: ${data.precipitation}\n" +
                "Wind Direction: ${data.windDirection}\n" +
                "Wind Speed: ${data.windSpeed}"
    }

    private fun displayLocationInfo(location: Location) {
        val locationInfo = findViewById<TextView>(R.id.locationInfo)
        locationInfo.text = "ID: ${location.id}\n" +
                "Address: ${location.address}\n" +
                "Latitude: ${location.latitude}\n" +
                "Longitude: ${location.longitude}"
    }
}
