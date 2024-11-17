package com.rasel.gozayaan.data.source

import com.rasel.gozayaan.data.models.RecommendationModel
import com.rasel.gozayaan.data.repository.HomeDataSource
import com.rasel.gozayaan.util.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeCacheDataSource @Inject constructor() : HomeDataSource {

    override fun getRecommendationList(): Flow<ApiResponse<List<RecommendationModel>>> {
        TODO("Not yet implemented")
    }
}
