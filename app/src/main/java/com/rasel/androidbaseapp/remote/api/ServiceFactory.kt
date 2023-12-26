package com.rasel.androidbaseapp.remote.api

import com.rasel.androidbaseapp.remote.utils.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceFactory {

    fun createCharacterService(
        isDebug: Boolean,
        baseUrl: String,
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): CharacterService {
        val retrofit = createRetrofit(isDebug, baseUrl, networkConnectionInterceptor)
        return retrofit.create(CharacterService::class.java)
    }

    fun create(
        isDebug: Boolean,
        baseUrl: String,
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): MyApi {
        val retrofit = createRetrofit(isDebug, baseUrl, networkConnectionInterceptor)
        return retrofit.create(MyApi::class.java)
    }

    private fun createRetrofit(
        isDebug: Boolean,
        baseUrl: String,
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                createOkHttpClient(
                    createLoggingInterceptor(isDebug),
                    networkConnectionInterceptor
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(networkConnectionInterceptor)
            .connectTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    private fun createLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (isDebug) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    private const val OK_HTTP_TIMEOUT = 60L

}
