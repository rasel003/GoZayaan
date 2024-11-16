package com.rasel.androidbaseapp.data.repository

import com.rasel.androidbaseapp.data.models.RecommendationModel
import kotlinx.coroutines.flow.Flow

interface HomeDataSource {
    suspend fun getRecommendationList(query: String): Flow<List<RecommendationModel>>

}
