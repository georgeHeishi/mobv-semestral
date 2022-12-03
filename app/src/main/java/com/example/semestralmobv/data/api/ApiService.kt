package com.example.semestralmobv.data.api

import android.content.Context
import com.example.semestralmobv.data.api.interceptors.AuthInterceptor
import com.example.semestralmobv.data.api.interceptors.TokenAuthenticator
import com.example.semestralmobv.data.api.models.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val HEADER_ACCESS = "Access-Control-Request-Headers: *"
private const val HEADER_API = "x-apikey: "
private const val HEADER_CONTENT_TYPE = "Content-Type: application/json"
private const val BASE_URL = "https://zadanie.mpage.sk/"
private const val FIND_ENDPOINT = "interpreter"

interface ApiService {
    @GET("https://overpass-api.de/api/interpreter?")
    fun getNearPubs(@Query("data") data: String): Call<PubsData>

    @GET("https://overpass-api.de/api/interpreter?")
    fun getPubDetail(@Query("data") data: String): Call<PubsData>

    @POST("user/create.php")
    fun signupUser(@Body user: UserArgs): Call<UserResponse>

    @POST("user/login.php")
    fun loginUser(@Body user: UserArgs): Call<UserResponse>

    @POST("user/refresh.php")
    fun refreshAccess(@Body refresh: UserRefreshArgs): Call<UserResponse>

    @GET("bar/list.php")
    @Headers("mobv-auth: accept")
    fun pubList(): Call<List<PubListResponse>>

    @POST("bar/message.php")
    @Headers("mobv-auth: accept")
    fun checkInPub(@Body pub: CheckInPubArgs): Call<Unit>

    companion object {
        fun create(context: Context): ApiService {
            val client =
                OkHttpClient.Builder().addInterceptor(AuthInterceptor(context)).authenticator(
                    TokenAuthenticator(context)
                ).build()
            val retrofit =
                Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                    .baseUrl(BASE_URL).build()
            return retrofit.create(ApiService::class.java);
        }
    }
}

