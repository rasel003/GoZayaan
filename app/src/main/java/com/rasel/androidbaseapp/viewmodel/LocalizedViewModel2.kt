package com.rasel.androidbaseapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasel.androidbaseapp.data.network.model.Localization
import com.rasel.androidbaseapp.data.network.responses.ProductListItem
import com.rasel.androidbaseapp.data.repositories.LocalizationRepository
import com.rasel.androidbaseapp.data.repositories.LocalizationRepository2
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
open class LocalizedViewModel2 @Inject constructor(
    private var localizationRepository: LocalizationRepository2
) : ViewModel() {

    private val _products = MutableLiveData<NetworkResult<List<ProductListItem>>>()
    val products: LiveData<NetworkResult<List<ProductListItem>>>
        get() = _products

    fun getProducts(){
        viewModelScope.launch {
            val result = localizationRepository.getProducts()
            _products.postValue(result)
        }
    }

}