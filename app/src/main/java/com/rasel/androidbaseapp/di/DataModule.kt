package com.rasel.androidbaseapp.di

import com.rasel.androidbaseapp.BuildConfig
import com.rasel.androidbaseapp.ui.settings.SettingsRepository
import com.rasel.androidbaseapp.ui.settings.SettingsRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /*@Provides
    @Singleton
    fun provideCharacterRepository(characterRepository: CharacterRepositoryImp): CharacterRepository =
        characterRepository*/

    @Provides
    @Singleton
    fun provideSettingRepository(): SettingsRepository =
        SettingsRepositoryImp(BuildConfig.VERSION_NAME)
}
