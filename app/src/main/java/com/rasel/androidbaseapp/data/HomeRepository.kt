package com.rasel.androidbaseapp.data

import android.os.Build
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.data.source.HomeDataSourceFactory
import com.rasel.androidbaseapp.remote.models.PostItem
import com.rasel.androidbaseapp.remote.models.UnsplashSearchResponse
import com.rasel.androidbaseapp.remote.utils.Resource
import com.rasel.androidbaseapp.remote.utils.SafeApiCall
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject

private const val MINIMUM_INTERVAL_DASHBOARD = 2


class HomeRepository @Inject constructor(
    private val dataSourceFactory: HomeDataSourceFactory
) {
    suspend fun getPostList(): Resource<List<PostItem>> = dataSourceFactory.getRemoteDataSource().getPostList()

    suspend fun getCurrentUserAvailableLeave(token: String) {
        val lastSavedAt = dataSourceFactory.getCacheDataSource().getSavedValue()

        if (lastSavedAt == null || isFetchNeeded(lastSavedAt)) {
            dataSourceFactory.getRemoteDataSource().getDashboardData(token)
        }
    }

    private fun getSavedTimeKey(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().toString()
        } else {
            Date().time.toString()
        }
    }

    //checking if last fetched time exceeded the interval time
    private fun isFetchNeeded(savedAt: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ChronoUnit.MINUTES.between(
                LocalDateTime.parse(savedAt),
                LocalDateTime.now()
            ) >= MINIMUM_INTERVAL_DASHBOARD
        } else {
            val interVal = Date().time - savedAt.toLong()
            interVal >= MINIMUM_INTERVAL_DASHBOARD * 1000
        }
    }

    fun getPlant(plantId: String) = dataSourceFactory.getCacheDataSource().getPlant(plantId)
    fun getPlants(): Flow<List<Plant>> = dataSourceFactory.getCacheDataSource().getPlants()
    suspend fun getDataFromUnSplash2(params: String): Flow<UnsplashSearchResponse> =
        dataSourceFactory.getRemoteDataSource().getDataFromUnSplash2(params)

    fun getPlantsWithGrowZoneNumber(zone: Int): Flow<List<Plant>> = dataSourceFactory.getCacheDataSource().getPlantsWithGrowZoneNumber(zone)
    fun getSearchResultStream(queryString: String) = dataSourceFactory.getRemoteDataSource().getSearchResultStream(queryString)
}