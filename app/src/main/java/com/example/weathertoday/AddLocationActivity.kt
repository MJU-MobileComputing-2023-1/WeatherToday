package com.example.weathertoday

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddLocationActivity : AppCompatActivity() {
    private lateinit var database: WeatherDatabase
    private var shouldNavigateToMain = false
    private var isNavigatingToMain = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)
        database = WeatherDatabase.getDatabase(this)
    }

    override fun onResume() {
        super.onResume()
        if (shouldNavigateToMain && !isNavigatingToMain) {
            navigateToMainActivity()
        } else {
            checkEmptyLocations()
        }
    }

    private fun checkEmptyLocations() {
        isNavigatingToMain = true
        lifecycleScope.launch(Dispatchers.IO) {
            val locations = database.locationDao().getAllLocations()
            withContext(Dispatchers.Main) {
                if (locations.isNotEmpty()) {
                    shouldNavigateToMain = true
                    navigateToMainActivity()
                } else {
                    isNavigatingToMain = false
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this@AddLocationActivity, MainActivity::class.java))
        finish()
    }

    fun onAddLocation(view: View) {
        val address = "" // Replace with actual address from user input
        val latitude = 0.0 // Replace with actual latitude from user input
        val longitude = 0.0 // Replace with actual longitude from user input

        val location = Location(id = 0, address = address, latitude = latitude, longitude = longitude)
        lifecycleScope.launch(Dispatchers.IO) {
            database.locationDao().insertLocation(location)
            withContext(Dispatchers.Main) {
                shouldNavigateToMain = true
                navigateToMainActivity()
            }
        }
    }

    fun onAddLocationButtonClick(view: View) {
        // SearchLocationActivity로 이동하도록 Intent 생성
        val intent = Intent(this, SearchLocationActivity::class.java)
        startActivity(intent)
    }
}
