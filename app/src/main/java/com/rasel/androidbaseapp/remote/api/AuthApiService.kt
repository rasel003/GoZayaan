package com.rasel.androidbaseapp.remote.api

import com.rasel.androidbaseapp.data.models.signin.Auth
import com.rasel.androidbaseapp.data.models.signin.LoginResponse
import com.rasel.androidbaseapp.data.models.signin.UserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(
        @Body auth: Auth,
    ): Response<LoginResponse>

    @GET("auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
    ): Response<LoginResponse>
}