import com.example.weathertoday.Weather

data class WeatherData(
    val temperature: Double,
    val humidity: Double,
    val rainfall1h: Double,
    val skyStatus: String,
    val eastWestWind: Double,
    val southNorthWind: Double,
    val precipitationType: Int,
    val lightning: String,
    val windDirection: String,
    val windSpeed: String,
    val date: String,
    val timestamp: String
) {
    fun toWeather(locationId: Int): Weather {
        return Weather(
            id = 0,
            locationId = locationId,
            temperature = this.temperature,
            humidity = this.humidity,
            rainfall1h = this.rainfall1h,
            skyStatus = this.skyStatus,
            eastWestWind = this.eastWestWind,
            southNorthWind = this.southNorthWind,
            precipitationType = this.precipitationType,
            lightning = this.lightning,
            windDirection = this.windDirection.toString(),
            windSpeed = this.windSpeed.toString(),
            date = this.date,
            timestamp = this.timestamp
        )
    }
}
