package com.rasel.androidbaseapp.di

import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProvider
import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProviderImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {

    @Provides
    @Singleton
    fun provideCoroutineContextProvider(contextProvider: CoroutineContextProviderImp): CoroutineContextProvider =
        contextProvider
}
