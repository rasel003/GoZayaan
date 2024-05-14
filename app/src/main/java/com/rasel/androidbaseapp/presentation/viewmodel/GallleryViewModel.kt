package com.rasel.androidbaseapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rasel.androidbaseapp.data.GalleryRepositoryImp
import com.rasel.androidbaseapp.data.models.TitleAndId
import com.rasel.androidbaseapp.data.models.signin.UserInfoResponse
import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProvider
import com.rasel.androidbaseapp.presentation.utils.ExceptionHandler
import com.rasel.androidbaseapp.presentation.utils.UiAwareLiveData
import com.rasel.androidbaseapp.presentation.utils.UiAwareModel
import com.rasel.androidbaseapp.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "rsl"

sealed class GalleryUIModel : UiAwareModel() {
    object Loading : GalleryUIModel()
    data class Error(var error: String = "") : GalleryUIModel()
    data class Success(val data: List<TitleAndId>) : GalleryUIModel()
}

@HiltViewModel
class GalleryViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val repositoryImp: GalleryRepositoryImp,
) : BaseViewModel(contextProvider) {

    private val _characterList = UiAwareLiveData<GalleryUIModel>()
    private var characterList: LiveData<GalleryUIModel> = _characterList

    private val _userInfoResponse = MutableLiveData<ApiResponse<List<TitleAndId>>>()
    val userInfoResponse = _userInfoResponse


    fun getImageList(): LiveData<GalleryUIModel> {
        return characterList
    }

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
        _characterList.postValue(GalleryUIModel.Error(exception.message ?: "Error"))
    }

    fun getUserInfo(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _userInfoResponse,
        coroutinesErrorHandler,
    ) {
        repositoryImp.getImageList2()
    }

    fun getImageList2() {

        Timber.tag(TAG).d("GalleryViewModel getImageList2: ")

        _characterList.postValue(GalleryUIModel.Loading)

        if (characterList.value is GalleryUIModel.Success) {

        }

        launchCoroutineIO {
            loadImages()
        }
    }

    private suspend fun loadImages() {
        repositoryImp.getImageList().collect {
            _characterList.postValue(GalleryUIModel.Success(it))
        }
    }

    override fun onCleared() {
        super.onCleared()

        Timber.tag(TAG).d(" GalleryViewModel onCleared: get called")

    }
}
