package com.example.semestralmobv.data.api.interceptors

import android.content.Context
import com.example.semestralmobv.data.api.ApiService
import com.example.semestralmobv.data.api.models.UserRefreshArgs
import com.example.semestralmobv.utils.PreferencesData
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Route


class TokenAuthenticator(val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: okhttp3.Response): Request? {
        synchronized(this) {
            if (response.request().header("mobv-auth")
                    ?.compareTo("accept") == 0 && response.code() == 401
            ) {
                val userItem = PreferencesData.getInstance().getUserItem(context)

                if (userItem == null) {
                    PreferencesData.getInstance().clearData(context)
                    return null
                }

                val tokenResponse = ApiService.create(context).refreshAccess(
                    UserRefreshArgs(
                        userItem.refresh
                    )
                ).execute()

                if (tokenResponse.isSuccessful) {
                    tokenResponse.body()?.let {
                        PreferencesData.getInstance().putUserItem(context, it)
                        return response.request().newBuilder()
                            .header("authorization", "Bearer ${it.access}")
                            .build()
                    }
                }

                PreferencesData.getInstance().clearData(context)
                return null


            }
        }
        return null
    }
}