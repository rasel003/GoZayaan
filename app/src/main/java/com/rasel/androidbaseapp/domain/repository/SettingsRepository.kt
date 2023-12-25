package com.rasel.androidbaseapp.domain.repository

import com.rasel.androidbaseapp.domain.models.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getSettings(isNightMode: Boolean): Flow<List<Settings>>
}
