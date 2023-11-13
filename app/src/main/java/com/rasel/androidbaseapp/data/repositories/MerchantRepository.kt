package com.rasel.androidbaseapp.data.repositories

import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.network.SafeApiCall
import javax.inject.Inject

class MerchantRepository @Inject constructor(
    private val api: MyApi,
) : SafeApiCall {

    suspend fun getNotifications(
    ) = safeApiCall {
        api.getNotifications()
    }

    /*  suspend fun saveAccessTokens(accessToken: String, refreshToken: String) {
          preferences.saveAccessTokens(accessToken, refreshToken)
      }*/

}