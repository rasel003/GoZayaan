package com.rasel.androidbaseapp.domain.repository

import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.util.AppLanguage
import kotlinx.coroutines.flow.StateFlow

interface LocalizationRepository {

    val localizationFlow: StateFlow<Localization>
    val currentAppLanguage: AppLanguage

    suspend fun updateLanguage(language: AppLanguage)
}