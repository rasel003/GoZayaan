package com.rasel.androidbaseapp.di

import android.content.Context
import com.rasel.androidbaseapp.cache.CharacterCacheImp
import com.rasel.androidbaseapp.cache.dao.CharacterDao
import com.rasel.androidbaseapp.cache.database.AppDatabase
import com.rasel.androidbaseapp.cache.dao.PlantDao
import com.rasel.androidbaseapp.data.preferences.PreferenceProvider
import com.rasel.androidbaseapp.data2.repository.CharacterCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CacheModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.invoke(context)
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
}
