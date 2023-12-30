package com.rasel.androidbaseapp.data.repository

import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.util.AppLanguage

interface LocalizationDataSource {
    // Remote and cache
    suspend fun getLocalization(language: AppLanguage): Localization

    // Cache
    fun getLocalizationFromCache(language: AppLanguage): Localization

    suspend fun saveLocalization(localization: Localization)
    suspend fun isCached(): Boolean
}
