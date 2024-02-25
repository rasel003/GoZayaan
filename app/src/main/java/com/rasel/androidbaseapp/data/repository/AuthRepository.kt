package com.rasel.androidbaseapp.data.repository

import com.rasel.androidbaseapp.data.models.signin.Auth
import com.rasel.androidbaseapp.remote.api.AuthApiService
import com.rasel.androidbaseapp.util.apiRequestFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
) {
    fun login(auth: Auth) = apiRequestFlow {
        authApiService.login(auth)
    }
}