package com.rasel.androidbaseapp.data.repository

import androidx.paging.PagingData
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.data.models.UnsplashPhoto
import com.rasel.androidbaseapp.remote.models.PostItem
import com.rasel.androidbaseapp.remote.models.UnsplashSearchResponse
import com.rasel.androidbaseapp.remote.utils.Resource
import com.rasel.androidbaseapp.util.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface HomeDataSource {
    // Remote and cache
    suspend fun getDashboardData(token: String)
    fun getPostList(): Flow<ApiResponse<List<PostItem>>>


    // Cache

    suspend fun getSavedValue(): String?
    fun getPlants(): Flow<List<Plant>>
    fun getPlant(plantId: String): Flow<Plant>
    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): Flow<List<Plant>>
    suspend fun isCached(): Boolean
    suspend fun getDataFromUnSplash2(query: String): Flow<UnsplashSearchResponse>
    suspend fun getDataFromUnSplash(query: String): Resource<Response<UnsplashSearchResponse>>
    fun getSearchResultStream(query: String): Flow<PagingData<UnsplashPhoto>>
}
