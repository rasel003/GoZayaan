package com.rasel.androidbaseapp.data

import com.rasel.androidbaseapp.data.models.RecommendationModel
import com.rasel.androidbaseapp.data.source.HomeDataSourceFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val dataSourceFactory: HomeDataSourceFactory
) {
    suspend fun getRecommendationList(params: String): Flow<List<RecommendationModel>> =
        dataSourceFactory.getRemoteDataSource().getRecommendationList(params)
}