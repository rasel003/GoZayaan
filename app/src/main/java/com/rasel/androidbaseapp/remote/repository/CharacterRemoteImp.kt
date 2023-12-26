package com.rasel.androidbaseapp.remote.repository

import com.rasel.androidbaseapp.data.models.CharacterEntity
import com.rasel.androidbaseapp.data.repository.CharacterRemote
import com.rasel.androidbaseapp.remote.mappers.CharacterEntityMapper
import com.rasel.androidbaseapp.remote.api.CharacterService
import javax.inject.Inject

class CharacterRemoteImp @Inject constructor(
    private val characterService: CharacterService,
    private val characterEntityMapper: CharacterEntityMapper
) : CharacterRemote {

    override suspend fun getCharacters(): List<CharacterEntity> {
        return characterService.getCharacters().characters.map { characterModel ->
            characterEntityMapper.mapFromModel(characterModel)
        }
    }

    override suspend fun getCharacter(characterId: Long): CharacterEntity {
        return characterEntityMapper.mapFromModel(characterService.getCharacter(characterId))
    }
}
