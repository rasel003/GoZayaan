package com.rasel.androidbaseapp.domain.interactor

import com.rasel.androidbaseapp.data.HomeRepository
import com.rasel.androidbaseapp.data.models.RecommendationModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetPhotoListBaseUseCase = BaseUseCase<String, Flow<List<RecommendationModel>>>

class PhotoListUseCase @Inject constructor(
    private val characterRepository: HomeRepository
) : GetPhotoListBaseUseCase {

    override suspend operator fun invoke(params: String) = characterRepository.getRecommendationList(params)
}