package com.rasel.androidbaseapp.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by linux64 on 9/26/16.
 */
object NetworkState {
    fun isNetworkAvailable(mContext: Context): Boolean {
        var status = false
        try {
            val cm = (mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                // connected to the internet
                when (activeNetwork.type) {
                    ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE ->
                        // connected to mobile data
                        // connected to wifi
                        status = true

                    else -> {}
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return status
    }
}
