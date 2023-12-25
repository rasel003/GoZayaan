package com.rasel.androidbaseapp.domain.interactor

import com.rasel.androidbaseapp.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias CharacterBookmarkBaseUseCase = BaseUseCase<Long, Flow<Int>>

class CharacterBookmarkUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) : CharacterBookmarkBaseUseCase {

    override suspend operator fun invoke(params: Long) =
        characterRepository.setCharacterBookmarked(params)
}
