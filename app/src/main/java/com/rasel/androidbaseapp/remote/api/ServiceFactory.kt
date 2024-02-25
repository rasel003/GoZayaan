package com.rasel.androidbaseapp.remote.api

import com.rasel.androidbaseapp.remote.utils.NetworkConnectionInterceptor
import com.rasel.androidbaseapp.util.AuthAuthenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceFactory {

    fun createAuthAPIService(
        baseUrl: String
    ): AuthApiService {
        val retrofit = createRetrofit(baseUrl)
        return retrofit.create(AuthApiService::class.java)
    }

    fun createMainAPIService(
        isDebug: Boolean,
        baseUrl: String,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authAuthenticator: AuthAuthenticator
    ): MainApiService {
        val retrofit = createRetrofit(isDebug, baseUrl, networkConnectionInterceptor, authAuthenticator)
        return retrofit.create(MainApiService::class.java)
    }

    fun createCharacterService(
        isDebug: Boolean,
        baseUrl: String,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authAuthenticator: AuthAuthenticator
    ): CharacterService {
        val retrofit =
            createRetrofit(isDebug, baseUrl, networkConnectionInterceptor, authAuthenticator)
        return retrofit.create(CharacterService::class.java)
    }

    fun create(
        isDebug: Boolean,
        baseUrl: String,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authAuthenticator: AuthAuthenticator
    ): MyApi {
        val retrofit =
            createRetrofit(isDebug, baseUrl, networkConnectionInterceptor, authAuthenticator)
        return retrofit.create(MyApi::class.java)
    }

    private fun createRetrofit(
        isDebug: Boolean,
        baseUrl: String,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authAuthenticator: AuthAuthenticator
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                createOkHttpClient(
                    createLoggingInterceptor(isDebug),
                    networkConnectionInterceptor,
                    authAuthenticator
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun createRetrofit(
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .authenticator(authAuthenticator)
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
