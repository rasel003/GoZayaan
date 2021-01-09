package com.rasel.androidbaseapp.data.repositories

import com.rasel.androidbaseapp.data.db.AppDatabase
import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.network.SafeApiRequest
import com.rasel.androidbaseapp.data.network.responses.UnsplashSearchResponse
import com.rasel.androidbaseapp.data.preferences.PreferenceProvider
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: MyApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest() {

    fun getUser() = db.getUserDao().getuser()

    /* suspend fun userLogin(email: String, password: String, fcmToken: String): LoginResponse {
       return apiRequest { api.userLogin(email, password, fcmToken) }
   }*/

   suspend fun getDataFromUnSplash(query: String) : UnsplashSearchResponse {
        return apiRequest { api.getDataFromUnSplash(query) }
    }

}