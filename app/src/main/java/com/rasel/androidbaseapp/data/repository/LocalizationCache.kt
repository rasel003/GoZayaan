package com.rasel.androidbaseapp.data.repository

import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.util.AppLanguage

interface LocalizationCache {
    fun getLocalization(language: AppLanguage): Localization
    suspend fun saveLocalization(localization: Localization)
    suspend fun isCached(): Boolean
    suspend fun setLastCacheTime(lastCache: Long)
    suspend fun isExpired(): Boolean
}
