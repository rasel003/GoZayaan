package com.rasel.androidbaseapp.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.rasel.androidbaseapp.util.AppLanguage

class PreferenceProvider(
    context: Context
) {

    companion object {
        private const val SHARED_PREFERENCE_KEY = "APP_SHARED_PREF"
        private const val KEY_APP_LANGUAGE = "KEY_APP_LANGUAGE"
        private const val PREF_KEY_LAST_CACHE = "last_cache"
        private const val PREF_KEY_NIGHT_MODE = "night_mode"
    }

    private val appContext = context.applicationContext

    private val preference: SharedPreferences by lazy {
        appContext.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
    }


    fun saveLastSavedAt(key: String, savedValue: String) {
        preference.edit().putString(
            key,
            savedValue
        ).apply()
    }

    fun clearSharedPreference() {
        preference.edit().clear().apply()
    }

    fun getSavedValue(key: String): String? {
        return preference.getString(key, null)
    }

    fun getAppLanguage(): AppLanguage {
        return try {
            val value = preference.getString(KEY_APP_LANGUAGE, "ENGLISH").orEmpty()
            AppLanguage.valueOf(value)
        } catch (e: Exception) {
            AppLanguage.ENGLISH
        }
    }

    suspend fun saveAppLanguage(language: AppLanguage) {
        preference
            .edit()
            .putString(KEY_APP_LANGUAGE, language.name)
            .apply()
    }

    var lastCacheTime: Long
        get() = preference.getLong(PREF_KEY_LAST_CACHE, 0)
        set(lastCache) = preference.edit().putLong(PREF_KEY_LAST_CACHE, lastCache).apply()

    var isNightMode: Boolean
        get() = preference.getBoolean(PREF_KEY_NIGHT_MODE, false)
        set(isDarkMode) = preference.edit().putBoolean(PREF_KEY_NIGHT_MODE, isDarkMode).apply()


}