package com.rasel.androidbaseapp.data.source

import com.rasel.androidbaseapp.data.models.RecommendationModel
import com.rasel.androidbaseapp.data.repository.HomeDataSource
import com.rasel.androidbaseapp.remote.api.MyApi
import com.rasel.androidbaseapp.remote.utils.SafeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val api: MyApi
) : HomeDataSource, SafeApiCall {

    override suspend fun getRecommendationList(query: String): Flow<List<RecommendationModel>> = flow {
        val response = api.getRecommendationList()
        emit(response)
    }
}
