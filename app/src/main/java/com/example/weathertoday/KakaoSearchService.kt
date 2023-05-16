// KakaoSearchService.kt
package com.example.weathertoday

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoSearchService {
    @Headers("Authorization: KakaoAK ed477ac16231bc9a775a548b9f93fc32")
    @GET("v2/local/search/address.json")
    suspend fun searchAddress(@Query("query") query: String): Response<AddressResult>


    companion object {
        private const val BASE_URL = "https://dapi.kakao.com/"

        fun create(): KakaoSearchService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KakaoSearchService::class.java)
        }
    }
}

data class AddressResult(
    val documents: List<AddressDocument>
)

data class AddressDocument(
    val address_name: String,
    val x: String, // longitude
    val y: String  // latitude
)
