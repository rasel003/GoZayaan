package com.rasel.androidbaseapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.remote.models.ProductListItem
import com.rasel.androidbaseapp.data.LocalizationRepositoryImp
import com.rasel.androidbaseapp.util.AppLanguage
import com.rasel.androidbaseapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Naing Aung Luu on 1/13/22.
 */
@HiltViewModel
open class LocalizedViewModel @Inject constructor(
    private var localizationRepositoryImp: LocalizationRepositoryImp
) : ViewModel() {

    val localizationFlow: StateFlow<Localization>
        get() = localizationRepositoryImp.localizationFlow

    val localization: Localization
        get() = localizationFlow.value

    val currentLanguageFlow: AppLanguage get() = localizationRepositoryImp.currentAppLanguage

    fun switchToEnglish() = switchLanguage(AppLanguage.ENGLISH)

    fun switchToChinese() = switchLanguage(AppLanguage.CHINESE)

    fun switchToBurmese() = switchLanguage(AppLanguage.BURMESE)

    private fun switchLanguage(language: AppLanguage) {
        viewModelScope.launch {
            localizationRepositoryImp.updateLanguage(language)
        }
    }
    fun getLocalizationFromRemote() {
        viewModelScope.launch {
            localizationRepositoryImp.getLocalizationFromRemote()
        }
    }


}