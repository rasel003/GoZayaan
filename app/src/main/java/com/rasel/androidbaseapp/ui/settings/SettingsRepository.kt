package com.rasel.androidbaseapp.ui.settings

import com.rasel.androidbaseapp.data.network.responses.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getSettings(isNightMode: Boolean): Flow<List<Settings>>
}
