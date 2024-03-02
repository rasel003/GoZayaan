package com.rasel.androidbaseapp.data

import android.os.Build
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rasel.androidbaseapp.cache.dao.PlantDao
import com.rasel.androidbaseapp.cache.preferences.PreferenceProvider
import com.rasel.androidbaseapp.data.models.UnsplashPhoto
import com.rasel.androidbaseapp.remote.api.MyApi
import com.rasel.androidbaseapp.remote.models.UnsplashSearchResponse
import com.rasel.androidbaseapp.remote.utils.SafeApiCall
import com.rasel.androidbaseapp.ui.gallery.UnsplashPagingSource
import com.rasel.androidbaseapp.util.KEY_DASHBOARD_LAST_SAVED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject

private const val MINIMUM_INTERVAL_DASHBOARD = 2


class HomeRepository @Inject constructor(
    private val api: MyApi,
    private val plantDao: PlantDao,
    private val prefs: PreferenceProvider
) : SafeApiCall {

    /* suspend fun userLogin(email: String, password: String, fcmToken: String): LoginResponse {
       return apiRequest { api.userLogin(email, password, fcmToken) }
   }*/

    fun getPlants() = plantDao.getPlants()

    fun getPlant(plantId: String) = plantDao.getPlant(plantId)

    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int) =
        plantDao.getPlantsWithGrowZoneNumber(growZoneNumber)

    suspend fun getDataFromUnSplash(query: String) = apiRequest { api.getDataFromUnSplash(query) }
    suspend fun getDataFromUnSplash2(query: String): Flow<UnsplashSearchResponse> = flow {
        val response = api.getDataFromUnSplash2(query)
        emit(response)
    }


    suspend fun getPostList() = apiRequest { api.getPostList() }

    fun getSearchResultStream(query: String): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { UnsplashPagingSource(api, query) }
        ).flow
    }

    suspend fun getCurrentUserAvailableLeave(token: String) {
        val lastSavedAt = prefs.getSavedValue(KEY_DASHBOARD_LAST_SAVED)

        if (lastSavedAt == null || isFetchNeeded(lastSavedAt)) {
            getDashboardDataFromNetwork(token)
        }
    }

    private suspend fun getDashboardDataFromNetwork(token: String) {

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

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
    }
}