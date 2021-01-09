package com.rasel.androidbaseapp.di

import android.content.Context
import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.network.NetworkConnectionInterceptor
import com.rasel.androidbaseapp.data.network.UnsplashService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideUnsplashService(): UnsplashService {
        return UnsplashService.create()
    }

    @Singleton
    @Provides
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }
    @Singleton
    @Provides
    fun provideMyApi(networkConnectionInterceptor: NetworkConnectionInterceptor): MyApi {
        return MyApi(networkConnectionInterceptor)
    }
}
