package com.rasel.androidbaseapp.remote.api

import com.rasel.androidbaseapp.data.models.RecommendationModel
import retrofit2.http.GET

interface MyApi {

    @GET("https://2e7c20f0c5b547a990453c4503464744.api.mockbin.io/")
    suspend fun getRecommendationList(): List<RecommendationModel>

}

