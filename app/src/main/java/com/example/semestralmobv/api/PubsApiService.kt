package com.example.semestralmobv.api

import com.example.semestralmobv.api.models.PubsArgs
import com.example.semestralmobv.api.models.PubsData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val HEADER_ACCESS = "Access-Control-Request-Headers: *"
private const val HEADER_API =
    "api-key: KHUu1Fo8042UwzczKz9nNeuVOsg2T4ClIfhndD2Su0G0LHHCBf0LnUF05L231J0M"
private const val HEADER_CONTENT_TYPE = "Content-Type: application/json"
private const val BASE_URL =
    "https://data.mongodb-api.com/app/data-fswjp/endpoint/data/v1/action/"
private const val FIND_ENDPOINT = "find"

interface PubsApiService {
    @POST(FIND_ENDPOINT)
    @Headers(HEADER_ACCESS, HEADER_API, HEADER_CONTENT_TYPE)
    fun getPubs(@Body userData: PubsArgs): Call<PubsData>
}

object PubsApi {
    private val retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
            .build()
    val retrofitService: PubsApiService by lazy {
        retrofit.create(PubsApiService::class.java)
    }
}