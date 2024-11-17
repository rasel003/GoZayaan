package com.rasel.gozayaan.data

import com.rasel.gozayaan.data.models.RecommendationModel
import com.rasel.gozayaan.data.source.HomeDataSourceFactory
import com.rasel.gozayaan.util.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val dataSourceFactory: HomeDataSourceFactory
) {
     fun getRecommendationList(): Flow<ApiResponse<List<RecommendationModel>>> =
        dataSourceFactory.getRemoteDataSource().getRecommendationList()
}