package com.rasel.androidbaseapp.domain.interactor

import com.rasel.androidbaseapp.domain.models.Character
import com.rasel.androidbaseapp.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetBookmarkCharacterListBaseUseCase = BaseUseCase<Unit, Flow<List<Character>>>

class GetBookmarkCharacterListUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) : GetBookmarkCharacterListBaseUseCase {

    override suspend operator fun invoke(params: Unit) = characterRepository.getBookMarkedCharacters()
}
