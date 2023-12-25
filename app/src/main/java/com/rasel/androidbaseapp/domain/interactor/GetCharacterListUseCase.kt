package com.rasel.androidbaseapp.domain.interactor

import com.rasel.androidbaseapp.domain.models.Character
import com.rasel.androidbaseapp.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetCharacterListBaseUseCase = BaseUseCase<Unit, Flow<List<Character>>>

class GetCharacterListUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) : GetCharacterListBaseUseCase {

    override suspend operator fun invoke(params: Unit) = characterRepository.getCharacters()
}
