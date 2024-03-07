package com.rasel.androidbaseapp.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.data.models.UnsplashPhoto
import com.rasel.androidbaseapp.data.repository.HomeDataSource
import com.rasel.androidbaseapp.remote.api.MyApi
import com.rasel.androidbaseapp.remote.models.UnsplashSearchResponse
import com.rasel.androidbaseapp.remote.utils.SafeApiCall
import com.rasel.androidbaseapp.ui.gallery.UnsplashPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val api: MyApi
) : HomeDataSource, SafeApiCall {
    override suspend fun getDashboardData(token: String) {

        /*withContext(Dispatchers.IO) {

            try {

                val response = apiRequest { api.getCurrentUserAvailableLeave(token) }

                response.dataCurrentUserAvailableLeave.userCurrentAvailableLeave.let {
                    userLeaveHistory.postValue(it)
                }
                response.dataCurrentUserAvailableLeave.status_info?.let {
                    statusInfoList.postValue(it)
                }
            } catch (e: NoInternetException) {
                Timber.d( e.message!!)
            } catch (e: ApiException) {
                Timber.d( e.message!!)
            } catch (e: Exception) {
                Timber.d( e.message ?: "Exception in bottom catch of current user available leave")
            }
        }*/
    }

    override suspend fun getPostList() = apiRequest { api.getPostList() }

    override suspend fun getDataFromUnSplash(query: String) =
        apiRequest { api.getDataFromUnSplash(query) }

    override suspend fun getDataFromUnSplash2(query: String): Flow<UnsplashSearchResponse> = flow {
        val response = api.getDataFromUnSplash2(query)
        emit(response)
    }

    override fun getSearchResultStream(query: String): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { UnsplashPagingSource(api, query) }
        ).flow
    }


    override fun getPlants(): Flow<List<Plant>> {
        throw UnsupportedOperationException("getSavedValue is not supported for RemoteDataSource.")
    }

    override fun getPlant(plantId: String): Flow<Plant> {
        throw UnsupportedOperationException("getSavedValue is not supported for RemoteDataSource.")
    }

    override fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): Flow<List<Plant>> {
        throw UnsupportedOperationException("getSavedValue is not supported for RemoteDataSource.")
    }

    override suspend fun getSavedValue(): String? {
        throw UnsupportedOperationException("getSavedValue is not supported for RemoteDataSource.")
    }

    override suspend fun isCached(): Boolean {
        throw UnsupportedOperationException("Cache is not supported for RemoteDataSource.")
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
    }
}
