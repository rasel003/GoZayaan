package com.rasel.androidbaseapp.data.source

import com.rasel.androidbaseapp.cache.dao.PlantDao
import com.rasel.androidbaseapp.cache.preferences.PreferenceProvider
import com.rasel.androidbaseapp.data.models.RecommendationModel
import com.rasel.androidbaseapp.data.repository.HomeDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeCacheDataSource @Inject constructor(
    private val plantDao: PlantDao,
    private val prefs: PreferenceProvider
) : HomeDataSource {

    override suspend fun getRecommendationList(query: String): Flow<List<RecommendationModel>> {
        TODO("Not yet implemented")
    }
}
