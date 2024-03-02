package com.rasel.androidbaseapp.domain.interactor

import com.rasel.androidbaseapp.data.HomeRepository
import com.rasel.androidbaseapp.remote.models.UnsplashSearchResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetPhotoListBaseUseCase = BaseUseCase<String, Flow<UnsplashSearchResponse>>

class PhotoListUseCase @Inject constructor(
    private val characterRepository: HomeRepository
) : GetPhotoListBaseUseCase {

    override suspend operator fun invoke(params: String) = characterRepository.getDataFromUnSplash2(params)
}