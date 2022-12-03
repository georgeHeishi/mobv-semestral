package com.example.semestralmobv.data.api.interceptors

import android.content.Context
import com.example.semestralmobv.utils.Config
import com.example.semestralmobv.utils.PreferencesData
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val request = chain.request()
                .newBuilder()
                .addHeader("User-Agent", "Mobv-Android/1.0.0")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")

            if (chain.request().header("mobv-auth")?.compareTo("accept") == 0) {
                request.addHeader(
                    "Authorization",
                    "Bearer ${PreferencesData.getInstance().getUserItem(context)?.access}"
                )

            }
            PreferencesData.getInstance().getUserItem(context)?.uid?.let {
                request.addHeader(
                    "x-user",
                    it
                )
            }
            request.addHeader("x-apikey", Config.API_KEY)

            return chain.proceed(request.build())
        }
    }

}