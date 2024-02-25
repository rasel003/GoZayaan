package com.rasel.androidbaseapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rasel.androidbaseapp.data.models.signin.Auth
import com.rasel.androidbaseapp.data.models.signin.LoginResponse
import com.rasel.androidbaseapp.data.repository.AuthRepository
import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProvider
import com.rasel.androidbaseapp.presentation.utils.ExceptionHandler
import com.rasel.androidbaseapp.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val authRepository: AuthRepository,
): BaseViewModel(contextProvider) {

    private val _loginResponse = MutableLiveData<ApiResponse<LoginResponse>>()
    val loginResponse = _loginResponse


    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
//        _character.postValue(CharacterDetailUIModel.Error(exception.message ?: "Error"))
    }

    fun login(auth: Auth, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _loginResponse,
        coroutinesErrorHandler
    ) {
        authRepository.login(auth)
    }
}