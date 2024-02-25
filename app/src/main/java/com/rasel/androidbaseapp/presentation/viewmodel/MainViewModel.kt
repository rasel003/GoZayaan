package com.rasel.androidbaseapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rasel.androidbaseapp.data.models.signin.UserInfoResponse
import com.rasel.androidbaseapp.data.repository.MainRepository
import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProvider
import com.rasel.androidbaseapp.presentation.utils.ExceptionHandler
import com.rasel.androidbaseapp.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val mainRepository: MainRepository,
): BaseViewModel(contextProvider) {

    private val _userInfoResponse = MutableLiveData<ApiResponse<UserInfoResponse>>()
    val userInfoResponse = _userInfoResponse

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
//        _character.postValue(CharacterDetailUIModel.Error(exception.message ?: "Error"))
    }

    fun getUserInfo(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _userInfoResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getUserInfo()
    }
}