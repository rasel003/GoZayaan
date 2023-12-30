package com.rasel.androidbaseapp.cache

import android.content.Context
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.data.models.LocalizationBundle
import com.rasel.androidbaseapp.data.repository.LocalizationCache
import com.rasel.androidbaseapp.util.AppLanguage
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalizationCacheImp @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalizationCache {

    private val moshiAdapter: JsonAdapter<LocalizationBundle> by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(LocalizationBundle::class.java)
    }

    override fun getLocalization(language: AppLanguage): Localization {
        val bundle = getLocalizationBundle()
        return bundle.getLocalization(language)
    }

    override suspend fun saveLocalization(localization: Localization) {
        TODO("Not yet implemented")
    }

    override suspend fun isCached(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun setLastCacheTime(lastCache: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun isExpired(): Boolean {
        TODO("Not yet implemented")
    }

    private fun getLocalizationBundle(): LocalizationBundle {
        // Fetch Localization Bundle from Raw Asset
        val localizationJson = getLocalizationJsonFromLocal()
        val localizationBundle = moshiAdapter.fromJson(localizationJson)
        return localizationBundle ?: LocalizationBundle()
    }

    private fun getLocalizationJsonFromLocal(): String = context.resources
        .openRawResource(R.raw.localization)
        .bufferedReader()
        .use { it.readText() }

    private fun LocalizationBundle.getLocalization(language: AppLanguage): Localization = when (language) {
        AppLanguage.ENGLISH -> en
        AppLanguage.CHINESE -> cn
        AppLanguage.BURMESE -> mm
    }

}