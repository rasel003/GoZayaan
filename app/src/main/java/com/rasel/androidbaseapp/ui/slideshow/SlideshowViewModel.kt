package com.rasel.androidbaseapp.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasel.androidbaseapp.data.network.Resource
import com.rasel.androidbaseapp.data.network.responses.PostItem
import com.rasel.androidbaseapp.data.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlideshowViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _notificationResponse: MutableLiveData<Resource<List<PostItem>>> =
        MutableLiveData()
    val notificationResponse: LiveData<Resource<List<PostItem>>> get() = _notificationResponse

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun getNotifications(
    ) = viewModelScope.launch {
        _notificationResponse.value = Resource.Loading
        _notificationResponse.value =
            repository.getPostList()
    }
}