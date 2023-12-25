package com.rasel.androidbaseapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rasel.androidbaseapp.data.preferences.PreferenceProvider
import com.rasel.androidbaseapp.domain.interactor.GetSettingsUseCase
import com.rasel.androidbaseapp.domain.models.Settings
import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProvider
import com.rasel.androidbaseapp.presentation.utils.ExceptionHandler
import com.rasel.androidbaseapp.presentation.utils.UiAwareModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

sealed class SettingUIModel : UiAwareModel() {
    object Loading : SettingUIModel()
    data class Error(var error: String = "") : SettingUIModel()
    data class Success(val data: List<Settings>) : SettingUIModel()
    data class NightMode(val nightMode: Boolean) : SettingUIModel()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val preferencesHelper: PreferenceProvider
) : BaseViewModel(contextProvider) {

    private val _settings = MutableLiveData<SettingUIModel>()
    val settings: LiveData<SettingUIModel> = _settings

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
        _settings.postValue(SettingUIModel.Error(exception.message ?: "Error"))
    }

    fun getSettings() {
        _settings.postValue(SettingUIModel.Loading)
        launchCoroutineIO {
            loadCharacters()
        }
    }

    private suspend fun loadCharacters() {
        getSettingsUseCase(preferencesHelper.isNightMode).collect {
            _settings.postValue(SettingUIModel.Success(it))
        }
    }

    fun setSettings(selectedSetting: Settings) {
        selectedSetting.run {
            preferencesHelper.isNightMode = selectedValue
            _settings.postValue(SettingUIModel.NightMode(selectedValue))
        }
    }
}
