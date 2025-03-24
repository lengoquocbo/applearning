package com.example.apphoctap.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString("ACCESS_TOKEN", token).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("ACCESS_TOKEN", null)
    }

    fun clearAccessToken() {
        sharedPreferences.edit().remove("ACCESS_TOKEN").apply()
    }
}