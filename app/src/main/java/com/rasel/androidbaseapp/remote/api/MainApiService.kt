package com.rasel.androidbaseapp.remote.api

import com.rasel.androidbaseapp.data.models.signin.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET

interface MainApiService {
    @GET("auth/profile")
    suspend fun getUserInfo(): Response<UserInfoResponse>
}