package ro.ubb.flaviu.mealplanner

import android.content.SharedPreferences
import ro.ubb.flaviu.mealplanner.data.AuthApi

fun putToken(sharedPref: SharedPreferences, token: String) {
    with(sharedPref.edit()) {
        putString("token", token)
        apply()
    }
}

fun removeToken(sharedPref: SharedPreferences) {
    with(sharedPref.edit()) {
        remove("token")
        apply()
    }
}

fun getToken(sharedPref: SharedPreferences): String? {
    val token = sharedPref.getString("token", null)
    AuthApi.updateToken(token)
    return token
}
