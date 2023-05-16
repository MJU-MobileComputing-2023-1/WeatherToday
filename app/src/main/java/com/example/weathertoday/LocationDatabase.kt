package com.example.weathertoday

import androidx.room.*

@Entity
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double
)

@Entity(foreignKeys = [ForeignKey(entity = Location::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("locationId"),
    onDelete = ForeignKey.CASCADE)])
data class Weather(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "locationId") val locationId: Int,
    @ColumnInfo(name = "temperature") val temperature: Double,
    @ColumnInfo(name = "humidity") val humidity: Double,
    @ColumnInfo(name = "rainfall1h") val rainfall1h: Double,
    @ColumnInfo(name = "skyStatus") val skyStatus: String,
    @ColumnInfo(name = "eastWestWind") val eastWestWind: Double,
    @ColumnInfo(name = "southNorthWind") val southNorthWind: Double,
    @ColumnInfo(name = "precipitationType") val precipitationType: Int,
    @ColumnInfo(name = "lightning") val lightning: String,
    @ColumnInfo(name = "windDirection") val windDirection: String,
    @ColumnInfo(name = "windSpeed") val windSpeed: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "timestamp") val timestamp: String
)


@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    fun getAllLocations(): List<Location>

    @Insert
    fun insertLocation(location: Location)

    @Delete
    fun deleteLocation(location: Location)

    @Query("DELETE FROM location")
    fun deleteAllLocations()

    @Query("SELECT * FROM location WHERE id = :id")
    fun getLocation(id: Int): Location
}

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE locationId = :locationId")
    fun getWeatherForLocation(locationId: Int): List<Weather>

    @Insert
    fun insertWeather(weather: Weather)

    @Query("DELETE FROM weather WHERE locationId = :locationId")
    fun deleteWeatherForLocation(locationId: Int)
}

@Database(entities = [Location::class, Weather::class], version = 2)  // 버전을 증가시켰습니다.
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun weatherDao(): WeatherDao
}
