package com.rasel.androidbaseapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasel.androidbaseapp.data.network.model.Localization
import com.rasel.androidbaseapp.data.network.responses.ProductListItem
import com.rasel.androidbaseapp.data.repositories.LocalizationRepository
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
    private var localizationRepository: LocalizationRepository
) : ViewModel() {

    val localizationFlow: StateFlow<Localization>
        get() = localizationRepository.localizationFlow

    val localization: Localization
        get() = localizationFlow.value

    val currentLanguageFlow: AppLanguage get() = localizationRepository.currentAppLanguage

    fun switchToEnglish() = switchLanguage(AppLanguage.ENGLISH)

    fun switchToChinese() = switchLanguage(AppLanguage.CHINESE)

    fun switchToBurmese() = switchLanguage(AppLanguage.BURMESE)

    private fun switchLanguage(language: AppLanguage) {
        viewModelScope.launch {
            localizationRepository.updateLanguage(language)
        }
    }
    fun getLocalizationFromRemote() {
        viewModelScope.launch {
            localizationRepository.getLocalizationFromRemote()
        }
    }


    private val _products = MutableLiveData<NetworkResult<List<ProductListItem>>>()
    val products: LiveData<NetworkResult<List<ProductListItem>>>
        get() = _products


}