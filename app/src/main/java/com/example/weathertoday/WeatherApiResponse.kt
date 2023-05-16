package com.example.weathertoday

data class WeatherApiResponse(
    val response: Response,
    val body: Body
) {
    data class Response(val header: Header)
    data class Header(val resultCode: String, val resultMsg: String)

    data class Body(val items: Items)
    data class Items(val item: List<ForecastItem>)

    data class ForecastItem(
        val baseDate: String,
        val baseTime: String,
        val category: String,
        val fcstDate: String,
        val fcstTime: String,
        val fcstValue: Double,
        val nx: Int,
        val ny: Int
    )
}
