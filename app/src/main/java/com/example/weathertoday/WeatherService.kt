package com.example.weathertoday

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URLEncoder

interface WeatherService {
    @GET("getUltraSrtFcst")
    suspend fun getWeather(
        @Query("serviceKey") apiKey: String,
        @Query("dataType") dataType: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): WeatherApiResponse

    companion object {
        const val API_KEY = "Rj7N4ncTPAmKly7pqUSlvLBtqd1spygWPYqifDXUEothQB9qx1AbuKtl4oitCvYHxTrlri4P9TDfsx%2BKw670UQ%3D%3D"

        fun create(): WeatherService {
            val encodedApiKey = URLEncoder.encode(API_KEY, "UTF-8")

            val retrofit = Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(WeatherService::class.java)
        }
    }
}