package com.rasel.androidbaseapp.di

import android.content.Context
import androidx.room.Room
import com.rasel.androidbaseapp.cache.CharacterCacheImp
import com.rasel.androidbaseapp.cache.LocalizationCacheImp
import com.rasel.androidbaseapp.cache.dao.CharacterDao
import com.rasel.androidbaseapp.cache.database.AppDatabase
import com.rasel.androidbaseapp.cache.dao.PlantDao
import com.rasel.androidbaseapp.cache.preferences.PreferenceProvider
import com.rasel.androidbaseapp.data.repository.CharacterCache
import com.rasel.androidbaseapp.data.repository.LocalizationCache
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [CacheModule::class])
@Module
class TestDatabaseModule {

    @Singleton
    @Provides
    fun provideTestDB(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun providePlantDao(appDatabase: AppDatabase): PlantDao {
        return appDatabase.getPlantDao()
    }

    @Provides
    fun providePreferenceProvider(@ApplicationContext context: Context): PreferenceProvider {
        return PreferenceProvider(context)
    }
    @Provides
    @Singleton
    fun provideCharacterDao(appDatabase: AppDatabase): CharacterDao {
        return appDatabase.cachedCharacterDao()
    }

    @Provides
    @Singleton
    fun provideCharacterCache(characterCache: CharacterCacheImp): CharacterCache {
        return characterCache
    }

    @Provides
    @Singleton
    fun provideLocalizationCache(localizationCache: LocalizationCacheImp): LocalizationCache {
        return localizationCache
    }
}