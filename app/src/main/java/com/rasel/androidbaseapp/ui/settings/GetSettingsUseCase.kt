package com.rasel.androidbaseapp.ui.settings

import com.rasel.androidbaseapp.data.network.responses.Settings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetSettingsBaseUseCase = BaseUseCase<Boolean, Flow<List<Settings>>>

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : GetSettingsBaseUseCase {

    override suspend operator fun invoke(params: Boolean) = settingsRepository.getSettings(params)
}