package com.rasel.gozayaan.data.repository

import com.rasel.gozayaan.data.models.RecommendationModel
import com.rasel.gozayaan.util.ApiResponse
import kotlinx.coroutines.flow.Flow

interface HomeDataSource {
     fun getRecommendationList(): Flow<ApiResponse<List<RecommendationModel>>>
}
