package com.rasel.androidbaseapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rasel.androidbaseapp.BuildConfig
import com.rasel.androidbaseapp.remote.utils.NetworkConnectionInterceptor
import com.rasel.androidbaseapp.remote.api.CharacterService
import com.rasel.androidbaseapp.remote.api.MyApi
import com.rasel.androidbaseapp.remote.api.ServiceFactory
import com.rasel.androidbaseapp.data.repository.CharacterRemote
import com.rasel.androidbaseapp.data.repository.LocalizationRemote
import com.rasel.androidbaseapp.remote.api.AuthApiService
import com.rasel.androidbaseapp.remote.api.MainApiService
import com.rasel.androidbaseapp.remote.repository.CharacterRemoteImp
import com.rasel.androidbaseapp.remote.repository.LocalizationRemoteImp
import com.rasel.androidbaseapp.util.AuthAuthenticator
import com.rasel.androidbaseapp.util.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManager(context)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
        AuthAuthenticator(tokenManager)

    @Singleton
    @Provides
    fun provideNetworkConnectionInterceptor(
        @ApplicationContext context: Context,
        tokenManager: TokenManager
    ): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(tokenManager, context)
    }

    @Provides
    @Singleton
    fun provideBlogService(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authAuthenticator: AuthAuthenticator
    ): CharacterService {
        return ServiceFactory.createCharacterService(
            BuildConfig.DEBUG,
            BuildConfig.BASE_URL,
            networkConnectionInterceptor,
            authAuthenticator
        )
    }

    @Singleton
    @Provides
    fun provideMyApi(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authAuthenticator: AuthAuthenticator
    ): MyApi {
        return ServiceFactory.create(
            BuildConfig.DEBUG,
            BuildConfig.BASE_URL,
            networkConnectionInterceptor,
            authAuthenticator
        )

    }

    @Singleton
    @Provides
    fun provideAuthAPIService(): AuthApiService =
        ServiceFactory.createAuthAPIService("https://api.escuelajs.co/api/v1/")

    @Singleton
    @Provides
    fun provideMainAPIService(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authAuthenticator: AuthAuthenticator
    ): MainApiService =
        ServiceFactory.createMainAPIService(
            BuildConfig.DEBUG,
            "https://api.escuelajs.co/api/v1/",
            networkConnectionInterceptor,
            authAuthenticator
        )

    @Provides
    @Singleton
    fun provideCharacterRemote(characterRemote: CharacterRemoteImp): CharacterRemote {
        return characterRemote
    }

    @Provides
    @Singleton
    fun provideLocalizationRemote(localizationRemote: LocalizationRemoteImp): LocalizationRemote {
        return localizationRemote
    }
}
