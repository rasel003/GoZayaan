package com.rasel.androidbaseapp.data.repository

import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.util.AppLanguage

interface LocalizationRemote {
    suspend fun getLocalization(language: AppLanguage): Localization

}
