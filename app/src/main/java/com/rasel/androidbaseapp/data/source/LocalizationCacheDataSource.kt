package com.rasel.androidbaseapp.data.source

import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.data.repository.LocalizationCache
import com.rasel.androidbaseapp.data.repository.LocalizationDataSource
import com.rasel.androidbaseapp.util.AppLanguage
import javax.inject.Inject

class LocalizationCacheDataSource @Inject constructor(
    private val localizationCache: LocalizationCache
) : LocalizationDataSource {

    override suspend fun getLocalization(language: AppLanguage): Localization {
        return localizationCache.getLocalization(language)
    }

    override fun getLocalizationFromCache(language: AppLanguage): Localization {
        return localizationCache.getLocalization(language)
    }

    override fun getAppLanguage() : AppLanguage {
        return localizationCache.getAppLanguage()
    }

    override suspend fun saveAppLanguage(language: AppLanguage) {
        localizationCache.saveAppLanguage(language)
    }

    override suspend fun saveLocalization(localization: Localization) {
        localizationCache.saveLocalization(localization)
        localizationCache.setLastCacheTime(System.currentTimeMillis())
    }

    override suspend fun isCached(): Boolean {
        return localizationCache.isCached()
    }
}
