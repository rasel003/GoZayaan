package com.rasel.androidbaseapp.data

import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.data.source.LocalizationDataSourceFactory
import com.rasel.androidbaseapp.domain.repository.LocalizationRepository
import com.rasel.androidbaseapp.util.AppLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalizationRepositoryImp @Inject constructor(
    private val dataSourceFactory: LocalizationDataSourceFactory
) : LocalizationRepository {


    private val _localizationFlow: MutableStateFlow<Localization> by lazy {
        MutableStateFlow(
            dataSourceFactory.getCacheDataSource().getLocalizationFromCache(currentAppLanguage)
        )
    }

    override val localizationFlow: StateFlow<Localization> = _localizationFlow

    override val currentAppLanguage: AppLanguage
        get() = dataSourceFactory.getCacheDataSource().getAppLanguage()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getLocalizationFromRemote()
        }
    }

    override suspend fun updateLanguage(language: AppLanguage) {
        dataSourceFactory.getCacheDataSource().saveAppLanguage(language)
        getLocalizationFromRemote()
    }

    suspend fun getLocalizationFromRemote() {
        // Fetch localization data from remote here
        dataSourceFactory.getRemoteDataSource().getLocalization(currentAppLanguage).let {
            _localizationFlow.value = it
        }
    }

   /* private fun getLocalization(language: AppLanguage) : Localization
            = cachedLocalizationBundle?.getLocalization(language)
        ?: local.getLocalizationBundle().getLocalization(language)*/




}