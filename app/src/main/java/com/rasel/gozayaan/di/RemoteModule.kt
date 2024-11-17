package com.rasel.gozayaan.di

import android.content.Context
import com.rasel.gozayaan.BuildConfig
import com.rasel.gozayaan.remote.api.MyApi
import com.rasel.gozayaan.remote.api.ServiceFactory
import com.rasel.gozayaan.remote.utils.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideMyApi(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
    ): MyApi {
        return ServiceFactory.createMyApiService(
            BuildConfig.DEBUG,
            BuildConfig.BASE_URL,
            networkConnectionInterceptor
        )

    }

    @Singleton
    @Provides
    fun provideNetworkConnectionInterceptor(
        @ApplicationContext context: Context,
    ): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }
}
