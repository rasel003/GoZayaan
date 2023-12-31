package com.rasel.androidbaseapp.data.source

import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.data.repository.LocalizationDataSource
import com.rasel.androidbaseapp.data.repository.LocalizationRemote
import com.rasel.androidbaseapp.util.AppLanguage
import javax.inject.Inject

class LocalizationRemoteDataSource @Inject constructor(
    private val localizationRemote: LocalizationRemote
) : LocalizationDataSource {

    override suspend fun getLocalization(language: AppLanguage): Localization {
      return  localizationRemote.getLocalization(language)
    }

    override fun getAppLanguage(): AppLanguage {
        throw UnsupportedOperationException("getAppLanguage is not supported for RemoteDataSource.")
    }

    override suspend fun saveAppLanguage(language: AppLanguage) {
        throw UnsupportedOperationException("Save AppLanguage is not supported for RemoteDataSource.")

    }

    override fun getLocalizationFromCache(language: AppLanguage): Localization {
        throw UnsupportedOperationException("getLocalizationFromCache is not supported for RemoteDataSource.")
    }

    override suspend fun saveLocalization(localization: Localization) {
        throw UnsupportedOperationException("Save character is not supported for RemoteDataSource.")
    }
    override suspend fun isCached(): Boolean {
        throw UnsupportedOperationException("Cache is not supported for RemoteDataSource.")
    }
}
