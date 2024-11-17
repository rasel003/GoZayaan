package com.rasel.gozayaan.remote.api

import com.rasel.gozayaan.data.models.RecommendationModel
import retrofit2.Response
import retrofit2.http.GET

interface MyApi {

    @GET("https://d9c8de84d7e4424dbbb59e258f353159.api.mockbin.io")
    suspend fun getRecommendationList(): Response<List<RecommendationModel>>
}

