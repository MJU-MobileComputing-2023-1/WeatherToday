package com.example.weathertoday

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

// SearchLocationActivity.kt
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
            // Add a null check for locationData
            if (locationData != null && locationData.documents.isNotEmpty()) {
                val document = locationData.documents[0]
                val location = Location(
                    id = 0,
                    address = document.address_name,
                    latitude = document.y.toDouble(),
                    longitude = document.x.toDouble()
                )

                // Save the location data to the database
                database.locationDao().insertLocation(location)

                val locations = database.locationDao().getAllLocations()
                withContext(Dispatchers.Main) {
                    val adapter = LocationAdapter(locations)
                    findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter
                }
            }
        }
    }
}
data class LocationResponse(val documents: List<Document>)

data class Document(
    val address_name: String,
    val x: Double,
    val y: Double
)