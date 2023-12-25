package com.rasel.androidbaseapp.domain.interactor

import com.rasel.androidbaseapp.domain.models.Character
import com.rasel.androidbaseapp.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetCharacterByIdBaseUseCase = BaseUseCase<Long, Flow<Character>>

class GetCharacterByIdUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) : GetCharacterByIdBaseUseCase {

    override suspend operator fun invoke(params: Long) = characterRepository.getCharacter(params)
}
