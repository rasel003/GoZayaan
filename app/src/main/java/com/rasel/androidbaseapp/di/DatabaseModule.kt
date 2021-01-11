package com.rasel.androidbaseapp.di

import android.content.Context
import com.rasel.androidbaseapp.data.db.AppDatabase
import com.rasel.androidbaseapp.data.db.dao.PlantDao
import com.rasel.androidbaseapp.data.preferences.PreferenceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.invoke(context)
    }

    @Provides
    fun providePlantDao(appDatabase: AppDatabase): PlantDao {
        return appDatabase.plantDao()
    }

    @Provides
    fun providePreferenceProvider(@ApplicationContext context: Context): PreferenceProvider {
        return PreferenceProvider(context)
    }
}
