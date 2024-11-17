package com.rasel.androidbaseapp.remote.api

import com.rasel.androidbaseapp.data.models.RecommendationModel
import retrofit2.http.GET

interface MyApi {

    @GET("https://d9c8de84d7e4424dbbb59e258f353159.api.mockbin.io")
    suspend fun getRecommendationList(): List<RecommendationModel>

}

