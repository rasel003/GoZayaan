package com.rasel.androidbaseapp.data.repositories

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rasel.androidbaseapp.data.db.AppDatabase
import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.network.SafeApiRequest
import com.rasel.androidbaseapp.data.preferences.PreferenceProvider
import com.rasel.androidbaseapp.util.ApiException
import com.rasel.androidbaseapp.util.Coroutines
import com.rasel.androidbaseapp.util.KEY_DASHBOARD_LAST_SAVED
import com.rasel.androidbaseapp.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

private const val MINIMUM_INTERVAL_DASHBOARD = 2

/*
class DashboardRepository(

    private val api: MyApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider

) : SafeApiRequest() {

    private val userLeaveHistory = MutableLiveData<List<UserCurrentAvailableLeave>>()
    private val statusInfoList = MutableLiveData<List<StatusInfo>>()

    init {
        userLeaveHistory.observeForever {
            saveUserLeaveHistory(it)
        }
        statusInfoList.observeForever {
            saveStatusInfoList(it)
        }
    }

    private fun saveStatusInfoList(list: List<StatusInfo>) {
        Coroutines.io {
            prefs.savelastSavedAt(KEY_DASHBOARD_LASTSAVED, getSavedTimeKey())
            db.getDashboardDao().clearHistoryFromStatusInfo()
            db.getDashboardDao().saveStatusInfo(list)
        }
    }

    private fun saveUserLeaveHistory(list: List<UserCurrentAvailableLeave>) {
        Coroutines.io {
            prefs.savelastSavedAt(KEY_DASHBOARD_LASTSAVED, getSavedTimeKey())
            db.getDashboardDao().clearHistoryFromUserCurrentAvailableLeave()

            val sex = prefs.getSavedValue(KEY_SEX)
            sex?.let {
                */
/*list.filter { crnt -> (crnt.leaveHead.equals("Maternity Leave") && sex.equals("male")) }
                db.getDashboardDao().saveUserCurrentAvailableLeave(list)*//*



                val newList : ArrayList<UserCurrentAvailableLeave> = ArrayList()

                list.forEach {
                    if (!it.leaveHead.equals("Maternity Leave") || sex.equals("female")) {
                        newList.add(it)
                    }
                }
                db.getDashboardDao().saveUserCurrentAvailableLeave(newList)
            }
        }
    }

    fun getUser() = db.getUserDao().getuser()


    suspend fun getCurrentUserAvailableLeave(token: String) {
        val lastSavedAt = prefs.getSavedValue(KEY_DASHBOARD_LAST_SAVED)

        if (lastSavedAt == null || isFetchNeeded(lastSavedAt)) {
            getDashboardDataFromNetwork(token)
        }
    }

    private suspend fun getDashboardDataFromNetwork(token: String) {

        withContext(Dispatchers.IO) {

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
}*/
