package com.rasel.androidbaseapp.domain.interactor

import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.data.HomeRepository
import com.rasel.androidbaseapp.domain.models.Character
import com.rasel.androidbaseapp.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetPlantListBaseUseCase = BaseUseCase<Unit, Flow<List<Plant>>>

class GetPlantListUseCase @Inject constructor(
    private val characterRepository: HomeRepository
) : GetPlantListBaseUseCase {

    override suspend operator fun invoke(params: Unit) = characterRepository.getPlants()
}