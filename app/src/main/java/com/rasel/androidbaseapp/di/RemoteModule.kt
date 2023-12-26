package com.rasel.androidbaseapp.di

import android.content.Context
import com.rasel.androidbaseapp.BuildConfig
import com.rasel.androidbaseapp.remote.utils.NetworkConnectionInterceptor
import com.rasel.androidbaseapp.remote.api.CharacterService
import com.rasel.androidbaseapp.remote.api.MyApi
import com.rasel.androidbaseapp.remote.api.ServiceFactory
import com.rasel.androidbaseapp.data.repository.CharacterRemote
import com.rasel.androidbaseapp.remote.repository.CharacterRemoteImp
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
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }
    @Provides
    @Singleton
    fun provideBlogService(networkConnectionInterceptor: NetworkConnectionInterceptor): CharacterService {
        return ServiceFactory.createCharacterService(BuildConfig.DEBUG, BuildConfig.BASE_URL, networkConnectionInterceptor)
    }
    @Singleton
    @Provides
    fun provideMyApi(networkConnectionInterceptor: NetworkConnectionInterceptor): MyApi {
        return ServiceFactory.create(BuildConfig.DEBUG, BuildConfig.BASE_URL, networkConnectionInterceptor)

    }
    @Provides
    @Singleton
    fun provideCharacterRemote(characterRemote: CharacterRemoteImp): CharacterRemote {
        return characterRemote
    }
}
