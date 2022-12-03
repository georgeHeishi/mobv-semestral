package com.example.semestralmobv.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.semestralmobv.R
import com.example.semestralmobv.data.api.models.UserResponse
import com.google.gson.Gson

class PreferencesData private constructor() {
    private fun getSharedPreferences(context: Context?): SharedPreferences? {
        return context?.getSharedPreferences(
            context.getString(R.string.preferences_key), Context.MODE_PRIVATE
        )
    }

    fun clearData(context: Context?) {
        context?.let { ViewModelFactoryProvider.provideDataRepository(it) }
        val sharedPref = getSharedPreferences(context) ?: return
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    fun putUserItem(context: Context?, userItem: UserResponse?) {
        val sharedPref = getSharedPreferences(context) ?: return
        val editor = sharedPref.edit()
        userItem?.let {
            editor.putString(context?.getString(R.string.preferences_key), Gson().toJson(it))
            editor.apply()
            return
        }
        editor.putString(context?.getString(R.string.preferences_key), null)
        editor.apply()
    }

    fun getUserItem(context: Context?): UserResponse? {
        val sharedPref = getSharedPreferences(context) ?: return null
        val json =
            sharedPref.getString(context?.getString(R.string.preferences_key), null) ?: return null

        return Gson().fromJson(json, UserResponse::class.java)
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferencesData? = null

        fun getInstance(): PreferencesData = INSTANCE ?: synchronized(this) {
            INSTANCE ?: PreferencesData().also { INSTANCE = it }
        }
    }
}