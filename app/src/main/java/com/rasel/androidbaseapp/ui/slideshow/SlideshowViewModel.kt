package com.rasel.androidbaseapp.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasel.androidbaseapp.data.network.NotificationResponse
import com.rasel.androidbaseapp.data.network.Resource
import com.rasel.androidbaseapp.data.repositories.MerchantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlideshowViewModel @Inject constructor(
    private val repository: MerchantRepository
) : ViewModel() {

    private val _notificationResponse: MutableLiveData<Resource<NotificationResponse>> =
        MutableLiveData()
    val notificationResponse: LiveData<Resource<NotificationResponse>> get() = _notificationResponse

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun getNotifications(
    ) = viewModelScope.launch {
        _notificationResponse.value = Resource.Loading
        _notificationResponse.value =
            repository.getNotifications()
    }
}