package com.rasel.androidbaseapp.data.repository

import com.rasel.androidbaseapp.remote.api.MainApiService
import com.rasel.androidbaseapp.util.apiRequestFlow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainApiService: MainApiService,
) {
    fun getUserInfo() = apiRequestFlow {
        mainApiService.getUserInfo()
    }
}