package com.example.weathertoday

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URLEncoder

interface WeatherService {
    @GET("getUltraSrtFcst")
    suspend fun getCurrentWeather(
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("base_date") base_date: String,
        @Query("base_time") base_time: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): WeatherApiResponse
}
