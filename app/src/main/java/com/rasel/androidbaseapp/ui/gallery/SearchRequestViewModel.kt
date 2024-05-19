package com.rasel.androidbaseapp.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rasel.androidbaseapp.data.HomeRepository
import com.rasel.androidbaseapp.data.models.SearchRequestParam
import com.rasel.androidbaseapp.data.models.UnsplashPhoto
import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProvider
import com.rasel.androidbaseapp.presentation.utils.ExceptionHandler
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchRequestViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val repository: HomeRepository
) : BaseViewModel(contextProvider) {

    var shouldWaitForNewUpdate: Boolean = false
    var searchQuery: MutableLiveData<SearchRequestParam> = MutableLiveData()
    var cloneSearchQuery: MutableLiveData<SearchRequestParam> = MutableLiveData()

    init {
        searchQuery.value = SearchRequestParam(search = "Beautiful Girl", page = 0)
    }

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
//        _characterList.postValue(CharacterUIModel.Error(exception.message ?: "Error"))
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared called on GalleryViewModel")
    }
}
