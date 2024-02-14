package com.rasel.androidbaseapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.rasel.androidbaseapp.cache.preferences.PreferenceProvider
import com.rasel.androidbaseapp.data.models.Theme
import com.rasel.androidbaseapp.domain.interactor.GetSettingsUseCase
import com.rasel.androidbaseapp.domain.interactor.SetThemeUseCase
import com.rasel.androidbaseapp.domain.interactor.settings.GetAvailableThemesUseCase
import com.rasel.androidbaseapp.domain.interactor.settings.GetThemeUseCase
import com.rasel.androidbaseapp.domain.models.Settings
import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProvider
import com.rasel.androidbaseapp.presentation.utils.ExceptionHandler
import com.rasel.androidbaseapp.presentation.utils.UiAwareModel
import com.rasel.androidbaseapp.util.result.Event
import com.rasel.androidbaseapp.util.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
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
    val setThemeUseCase: SetThemeUseCase,
    getAvailableThemesUseCase: GetAvailableThemesUseCase,
    getThemeUseCase: GetThemeUseCase,
    private val preferencesHelper: PreferenceProvider
) : BaseViewModel(contextProvider) {

    private val _settings = MutableLiveData<SettingUIModel>()
    val settings: LiveData<SettingUIModel> = _settings

    private val _navigateToLanguageSelector = MutableLiveData<Event<Unit>>()
    private val _navigateToThemeSelector = MutableLiveData<Event<Unit>>()
    val navigateToThemeSelector: LiveData<Event<Unit>>
        get() = _navigateToThemeSelector
    val navigateToLanguageSelector: LiveData<Event<Unit>>
        get() = _navigateToLanguageSelector

    // Theme setting
    val theme: LiveData<Theme> = liveData {
        emit(getThemeUseCase(Unit).successOr(Theme.SYSTEM))
    }

    // Theme setting
    val availableThemes: LiveData<List<Theme>> = liveData {
        emit(getAvailableThemesUseCase(Unit).successOr(emptyList()))
    }

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

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            setThemeUseCase(theme)
        }
    }

    fun onThemeSettingClicked() {
        _navigateToThemeSelector.value = Event(Unit)
    }

    fun onLanguageSettingClicked() {
        _navigateToLanguageSelector.value = Event(Unit)
    }
}
