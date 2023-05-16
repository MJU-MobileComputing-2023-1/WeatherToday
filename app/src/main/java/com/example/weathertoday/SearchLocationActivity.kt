package com.example.weathertoday

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import android.util.Log

class SearchLocationActivity : AppCompatActivity() {

    private lateinit var database: WeatherDatabase
    private lateinit var kakaoSearchService: KakaoSearchService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)

        database = WeatherDatabase.getDatabase(this)
        kakaoSearchService = KakaoSearchService.create()
    }

    fun onSearchLocation(view: View) {
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val searchQuery = searchEditText.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            val response = kakaoSearchService.searchAddress(searchQuery)
            val locationData = response.body()
            if (locationData != null && locationData.documents.isNotEmpty()) {
                val document = locationData.documents[0]
                val location = Location(
                    id = 0,
                    address = document.address_name,
                    latitude = document.y.toDouble(),
                    longitude = document.x.toDouble()
                )
                Log.d("TEST","$location")

                try {
                    database.locationDao().insertLocation(location)
                    Log.d("TEST","DATA inserted")
                }catch(e: NumberFormatException){
                    Log.d("TEST","DATA not inserted")
                }


                val locations = database.locationDao().getAllLocations()
                withContext(Dispatchers.Main) {
                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                    val adapter = LocationAdapter(locations) { selectedLocation ->
                        // Save the selected location ID to SharedPreferences
                        val sharedPreferences = getSharedPreferences("WeatherToday", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putInt("selectedLocationId", selectedLocation.id).apply()

                        // Return to MainActivity
                        val intent = Intent(this@SearchLocationActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    recyclerView.adapter = adapter
                }
            }
        }
    }
}
