package com.rasel.androidbaseapp.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasel.androidbaseapp.remote.utils.Resource
import com.rasel.androidbaseapp.remote.models.PostItem
import com.rasel.androidbaseapp.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlideshowViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _postList: MutableLiveData<Resource<List<PostItem>>> =
        MutableLiveData()
    val postList: LiveData<Resource<List<PostItem>>> get() = _postList

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun getPostList(
    ) = viewModelScope.launch {
//        _postList.value = Resource.Loading
        _postList.value =
            repository.getPostList()
    }
}