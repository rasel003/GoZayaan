package com.rasel.gozayaan.data.source

import com.rasel.gozayaan.data.models.RecommendationModel
import com.rasel.gozayaan.data.repository.HomeDataSource
import com.rasel.gozayaan.remote.api.MyApi
import com.rasel.gozayaan.remote.utils.SafeApiCall
import com.rasel.gozayaan.util.apiRequestFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val api: MyApi
) : HomeDataSource, SafeApiCall {

    override fun getRecommendationList() =  apiRequestFlow { api.getRecommendationList() }
}
