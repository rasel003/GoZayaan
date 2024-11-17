package com.rasel.gozayaan.remote.utils

import android.content.Context
import com.rasel.gozayaan.util.isNetworkAvailable
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkConnectionInterceptor @Inject constructor(
    context: Context
) : Interceptor {

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
       /* if (!applicationContext.isNetworkAvailable()) throw NoInternetException("Make sure you have an active data connection")
        return chain.proceed(chain.request())*/


        var request = chain.request()
        request = if (applicationContext.isNetworkAvailable()) {
            request.newBuilder()
                .header("Cache-Control", "public, max-age=" + 5)
//                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                .build()
        }
        return chain.proceed(request)
    }
}