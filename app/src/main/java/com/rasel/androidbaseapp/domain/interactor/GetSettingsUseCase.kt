package com.rasel.androidbaseapp.domain.interactor

import com.rasel.androidbaseapp.domain.models.Settings
import com.rasel.androidbaseapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetSettingsBaseUseCase = BaseUseCase<Boolean, Flow<List<Settings>>>

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : GetSettingsBaseUseCase {

    override suspend operator fun invoke(params: Boolean) = settingsRepository.getSettings(params)
}
