package com.rasel.androidbaseapp.data.source

import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.data.repository.LocalizationCache
import com.rasel.androidbaseapp.data.repository.LocalizationDataSource
import com.rasel.androidbaseapp.util.AppLanguage
import javax.inject.Inject

class LocalizationCacheDataSource @Inject constructor(
    private val characterCache: LocalizationCache
) : LocalizationDataSource {

    override suspend fun getLocalization(language: AppLanguage): Localization {
        return characterCache.getLocalization(language)
    }

    override fun getLocalizationFromCache(language: AppLanguage): Localization {
        return characterCache.getLocalization(language)
    }

    override suspend fun saveLocalization(localization: Localization) {
        characterCache.saveLocalization(localization)
        characterCache.setLastCacheTime(System.currentTimeMillis())
    }
    override suspend fun isCached(): Boolean {
        return characterCache.isCached()
    }
}
