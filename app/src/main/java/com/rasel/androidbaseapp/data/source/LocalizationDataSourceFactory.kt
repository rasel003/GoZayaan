package com.rasel.androidbaseapp.data.source

import com.rasel.androidbaseapp.data.repository.LocalizationCache
import com.rasel.androidbaseapp.data.repository.LocalizationDataSource
import javax.inject.Inject

open class LocalizationDataSourceFactory @Inject constructor(
    private val characterCache: LocalizationCache,
    private val cacheDataSource: LocalizationCacheDataSource,
    private val remoteDataSource: LocalizationRemoteDataSource
) {

    open suspend fun getDataStore(isCached: Boolean): LocalizationDataSource {
        return if (isCached && !characterCache.isExpired()) {
            return getCacheDataSource()
        } else {
            getRemoteDataSource()
        }
    }

    fun getRemoteDataSource(): LocalizationDataSource {
        return remoteDataSource
    }

    fun getCacheDataSource(): LocalizationDataSource {
        return cacheDataSource
    }
}
