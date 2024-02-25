package com.rasel.androidbaseapp.di

import com.rasel.androidbaseapp.data.repository.AuthRepository
import com.rasel.androidbaseapp.data.repository.MainRepository
import com.rasel.androidbaseapp.remote.api.AuthApiService
import com.rasel.androidbaseapp.remote.api.MainApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    @Provides
    fun provideAuthRepository(authApiService: AuthApiService) = AuthRepository(authApiService)

    @Provides
    fun provideMainRepository(mainApiService: MainApiService) = MainRepository(mainApiService)
}