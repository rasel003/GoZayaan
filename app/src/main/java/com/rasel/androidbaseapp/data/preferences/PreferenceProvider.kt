package com.rasel.androidbaseapp.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PreferenceProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun saveLastSavedAt(key: String, savedValue: String) {
        preference.edit().putString(
            key,
            savedValue
        ).apply()
    }
    fun clearSharedPreference(){
        preference.edit().clear().apply()
    }

    fun getSavedValue(key: String): String? {
        return preference.getString(key, null)
    }

}