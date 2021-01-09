package com.rasel.androidbaseapp.ui.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rasel.androidbaseapp.data.network.model.UnsplashPhoto
import com.rasel.androidbaseapp.data.repositories.UnsplashRepository
import kotlinx.coroutines.flow.Flow

class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {
    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<UnsplashPhoto>>? = null

    fun searchPictures(queryString: String): Flow<PagingData<UnsplashPhoto>> {
        currentQueryValue = queryString
        val newResult: Flow<PagingData<UnsplashPhoto>> = repository.getSearchResultStream(queryString).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}
