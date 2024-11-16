package com.rasel.androidbaseapp.remote.utils

import android.content.Context
import com.rasel.androidbaseapp.cache.preferences.TokenManager
import com.rasel.androidbaseapp.util.isNetworkAvailable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkConnectionInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    context: Context
) : Interceptor {

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
       /* if (!applicationContext.isNetworkAvailable()) throw NoInternetException("Make sure you have an active data connection")
        return chain.proceed(chain.request())*/

        val token = runBlocking {
            tokenManager.getToken().first()
        }

        var request = chain.request()
        request = if (applicationContext.isNetworkAvailable()) {
            request.newBuilder()
                .header("Cache-Control", "public, max-age=" + 5)
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                .build()
        }
        return chain.proceed(request)
    }
}