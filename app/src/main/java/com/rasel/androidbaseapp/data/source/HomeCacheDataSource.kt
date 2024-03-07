package com.rasel.androidbaseapp.data.source

import androidx.paging.PagingData
import com.rasel.androidbaseapp.cache.dao.PlantDao
import com.rasel.androidbaseapp.cache.preferences.PreferenceProvider
import com.rasel.androidbaseapp.data.models.UnsplashPhoto
import com.rasel.androidbaseapp.data.repository.CharacterCache
import com.rasel.androidbaseapp.data.repository.HomeDataSource
import com.rasel.androidbaseapp.remote.models.PostItem
import com.rasel.androidbaseapp.remote.models.UnsplashSearchResponse
import com.rasel.androidbaseapp.remote.utils.Resource
import com.rasel.androidbaseapp.util.KEY_DASHBOARD_LAST_SAVED
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class HomeCacheDataSource @Inject constructor(
    private val plantDao: PlantDao,
    private val prefs: PreferenceProvider
) : HomeDataSource {

    override fun getPlants() = plantDao.getPlants()
    override fun getPlant(plantId: String) = plantDao.getPlant(plantId)

    override fun getPlantsWithGrowZoneNumber(growZoneNumber: Int) =
        plantDao.getPlantsWithGrowZoneNumber(growZoneNumber)

    override suspend fun getSavedValue(): String? {
        return prefs.getSavedValue(KEY_DASHBOARD_LAST_SAVED)
    }

    override suspend fun getDashboardData(token: String) {
    }

    override suspend fun getPostList(): Resource<List<PostItem>> {
        return Resource.Success(emptyList())
    }

    override suspend fun isCached(): Boolean {
        return false
    }

    override suspend fun getDataFromUnSplash2(query: String): Flow<UnsplashSearchResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getDataFromUnSplash(query: String): Resource<Response<UnsplashSearchResponse>> {
        TODO("Not yet implemented")
    }

    override fun getSearchResultStream(query: String): Flow<PagingData<UnsplashPhoto>> {
        TODO("Not yet implemented")
    }
}
