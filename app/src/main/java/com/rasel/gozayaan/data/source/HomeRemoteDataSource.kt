package com.rasel.gozayaan.data.source

import com.rasel.gozayaan.data.repository.HomeDataSource
import com.rasel.gozayaan.remote.api.MyApi
import com.rasel.gozayaan.util.apiRequestFlow
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val api: MyApi
) : HomeDataSource {

    override fun getRecommendationList() =  apiRequestFlow { api.getRecommendationList() }
}
