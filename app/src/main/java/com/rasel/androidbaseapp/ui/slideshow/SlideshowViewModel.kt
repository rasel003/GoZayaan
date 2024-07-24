package com.rasel.androidbaseapp.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasel.androidbaseapp.remote.utils.Resource
import com.rasel.androidbaseapp.remote.models.PostItem
import com.rasel.androidbaseapp.data.HomeRepository
import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProvider
import com.rasel.androidbaseapp.presentation.utils.ExceptionHandler
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.CoroutinesErrorHandler
import com.rasel.androidbaseapp.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlideshowViewModel @Inject constructor(
    private val repository: HomeRepository,
    contextProvider: CoroutineContextProvider,
) : BaseViewModel(contextProvider) {

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
//        _character.postValue(CharacterDetailUIModel.Error(exception.message ?: "Error"))
    }



    private val _postList: MutableLiveData<ApiResponse<List<PostItem>>> = MutableLiveData()
    val postList: LiveData<ApiResponse<List<PostItem>>> get() = _postList

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    init {
        getPost()
    }

    fun getPostList(
        coroutinesErrorHandler: CoroutinesErrorHandler
    ) = baseRequest(
        _postList,
        coroutinesErrorHandler
    ) {
        repository.getPostList()
    }

    private fun getPost() {
        viewModelScope.launch {
            repository.getPost().collect{
                if(it.first == "Success"){
                    val outPut = it.second
                }
            }
        }

    }
}