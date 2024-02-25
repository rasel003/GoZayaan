package com.rasel.androidbaseapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rasel.androidbaseapp.data.models.signin.Auth
import com.rasel.androidbaseapp.data.models.signin.LoginResponse
import com.rasel.androidbaseapp.data.repository.AuthRepository
import com.rasel.androidbaseapp.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): BaseViewModel2() {

    private val _loginResponse = MutableLiveData<ApiResponse<LoginResponse>>()
    val loginResponse = _loginResponse

    fun login(auth: Auth, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _loginResponse,
        coroutinesErrorHandler
    ) {
        authRepository.login(auth)
    }
}