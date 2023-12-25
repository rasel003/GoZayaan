package com.rasel.androidbaseapp.data2.source

import com.rasel.androidbaseapp.data2.repository.CharacterCache
import com.rasel.androidbaseapp.data2.repository.CharacterDataSource
import javax.inject.Inject

open class CharacterDataSourceFactory @Inject constructor(
    private val characterCache: CharacterCache,
    private val cacheDataSource: CharacterCacheDataSource,
    private val remoteDataSource: CharacterRemoteDataSource
) {

    open suspend fun getDataStore(isCached: Boolean): CharacterDataSource {
        return if (isCached && !characterCache.isExpired()) {
            return getCacheDataSource()
        } else {
            getRemoteDataSource()
        }
    }

    fun getRemoteDataSource(): CharacterDataSource {
        return remoteDataSource
    }

    fun getCacheDataSource(): CharacterDataSource {
        return cacheDataSource
    }
}
